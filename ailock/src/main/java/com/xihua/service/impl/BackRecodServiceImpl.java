package com.xihua.service.impl;

import com.xihua.bean.SysUser;
import com.xihua.exception.BusinessException;
import com.xihua.manager.AsyncFactory;
import com.xihua.manager.AsyncManager;
import com.xihua.service.ISysUserService;
import com.xihua.utils.DateUtils;
import com.xihua.utils.ServletUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xihua.mapper.BackRecodMapper;
import com.xihua.bean.BackRecod;
import com.xihua.service.IBackRecodService;
import com.xihua.base.AbstractService;
import com.xihua.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 行车记录 服务层实现
 *
 * @author admin
 * @date 2019-12-04
 */
@Service
public class BackRecodServiceImpl extends AbstractService<BackRecod, Integer> implements IBackRecodService {

    @Autowired
    private BackRecodMapper backRecodMapper;

    @Autowired
    private IBackRecodService backRecodService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    public void setBaseMapper() {
        super.setBaseMapper(backRecodMapper);
    }

    /**
     * 新增行车记录
     *
     * @param backRecod 行车记录信息
     * @return 结果
     */
    @Override
    @Transactional
    public int saveOrUpdateBackRecod(BackRecod backRecod) {
        // 新记录标记
        boolean isNewRecord = false;
        if (StringUtils.isEmpty(backRecod.getId())) {
            isNewRecord = true;
            backRecodService.insert(backRecod);
        } else {
            backRecodService.update(backRecod);
        }
        return 1;
    }

    @Override
    public BackRecod selectByRunningRecord(Integer userId, String backId) {
        return backRecodMapper.selectByRunningRecord(userId, backId);
    }

    @Override
    public int buildOpenRecord(BackRecod openRecod, SysUser sysUser) {
        Date now = new Date();
        openRecod.setUserId(sysUser.getUserId());
        openRecod.setStartTime(now);
        openRecod.setStatus("0");
        // scoket 通知开锁
        AsyncManager.asyncManager().execute(AsyncFactory.syncSendOpen(openRecod.getBackId()));
        return backRecodService.saveOrUpdateBackRecod(openRecod);
    }

    @Override
    public void reBack(String backId) {
        SysUser currentUser = null;
        BackRecod runningRecord = null;
        currentUser = ServletUtil.getOnlineUser();
        if (StringUtils.isEmpty(currentUser)) {
            runningRecord = backRecodService.selectByRunningRecord(backId);
            currentUser = sysUserService.selectById(runningRecord.getUserId());
        } else {
            runningRecord = backRecodService.selectByRunningRecord(currentUser.getUserId(), backId);
        }
        if (StringUtils.isEmpty(runningRecord)) {
            // 发送还车失败消息
            simpMessagingTemplate.convertAndSend("/stop/back/" + currentUser.getPhone(), "error");
        }
        // 更新状态并计费
        updateRecord(runningRecord);
        backRecodService.saveOrUpdateBackRecod(runningRecord);
        // 向客户端推送消息
        simpMessagingTemplate.convertAndSend("/stop/back/" + currentUser.getPhone(), "success");
    }

    @Override
    public BackRecod selectByRunningRecord(Integer userId) {
        return backRecodMapper.selectSingleRunningRecord(userId);
    }

    @Override
    public List<BackRecod> selectAllStopRecord(Integer userId) {
        return backRecodMapper.selectAllStopRecord(userId);
    }

    @Override
    public BackRecod selectByRunningRecord(String backId) {
        return backRecodMapper.selectSingleRunningRecordByBack(backId);
    }

    private void updateRecord(BackRecod runningRecord) {
        // 修改状态
        Date now = new Date();
        runningRecord.setEndTime(now);
        runningRecord.setStatus("1");
        // 转成多少个半小时
        int timeSpace = (int) DateUtils.getHalfHour(now, runningRecord.getStartTime());
        // 计费
        runningRecord.setTPrice(runningRecord.getPrice() * timeSpace);
    }
}
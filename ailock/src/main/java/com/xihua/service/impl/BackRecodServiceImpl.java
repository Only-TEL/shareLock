package com.xihua.service.impl;

import com.xihua.bean.SysUser;
import com.xihua.manager.AsyncFactory;
import com.xihua.manager.AsyncManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xihua.mapper.BackRecodMapper;
import com.xihua.bean.BackRecod;
import com.xihua.service.IBackRecodService;
import com.xihua.base.AbstractService;
import com.xihua.utils.StringUtils;

import java.util.Date;

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
        // todo scoket 通知开锁
        // AsyncManager.asyncManager().execute(AsyncFactory.syncSendOpen(openRecod.getBackId()));
        return backRecodService.saveOrUpdateBackRecod(openRecod);
    }

}
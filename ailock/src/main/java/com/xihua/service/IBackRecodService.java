package com.xihua.service;

import com.xihua.bean.BackRecod;
import com.xihua.base.BaseService;
import com.xihua.bean.SysUser;

import java.util.List;

/**
 * 行车记录 服务层
 *
 * @author admin
 * @date 2019-12-04
 */
public interface IBackRecodService extends BaseService<BackRecod, Integer> {

    // 保存或更新
    public int saveOrUpdateBackRecod(BackRecod backRecod);

    // 查询存在的已开锁记录
    BackRecod selectByRunningRecord(Integer userId, String backId);

    // 生成开锁记录
    int buildOpenRecord(BackRecod openRecod, SysUser sysUser);

    // 还车操作
    void reBack(String backId);

    // 查询存在的已开锁记录
    BackRecod selectByRunningRecord(Integer userId);

    // 查询开锁历史
    List<BackRecod> selectAllStopRecord(Integer userId);

    // 查询存在的已开锁记录
    BackRecod selectByRunningRecord(String backId);
}
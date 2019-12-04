package com.xihua.service;

import com.xihua.bean.SysUser;
import com.xihua.base.BaseService;

import java.util.List;

/**
 * 用户 服务层
 *
 * @author admin
 * @date 2019-12-04
 */
public interface ISysUserService extends BaseService<SysUser, Integer> {

    // 保存或更新
    public int saveOrUpdateSysUser(SysUser sysUser);

    // 注册
    public boolean register(SysUser sysUser);

    // 登录
    public SysUser login(String userName, String password);

    // 用户名登录
    SysUser selectUserByLoginName(String userName);

    // 手机号登录
    SysUser selectUserByPhoneNumber(String phone);

    // 验证密码
    boolean validate(SysUser sysUser, String password);
}
package com.xihua.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xihua.mapper.SysUserMapper;
import com.xihua.bean.SysUser;
import com.xihua.service.ISysUserService;
import com.xihua.annotation.SaveOrUpdate;

/**
 * 用户 服务层实现
 *
 * @author admin
 * @date 2019-12-03
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public SysUser selectSysUserById(String userId) {
        return sysUserMapper.selectSysUserById(userId);
    }

    /**
     * 查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户集合
     */
    @Override
    public List<SysUser> selectSysUserList(SysUser sysUser) {
        return sysUserMapper.selectSysUserList(sysUser);
    }

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    @Override
    @SaveOrUpdate
    public int insertSysUser(SysUser sysUser) {
        return sysUserMapper.insertSysUser(sysUser);
    }

    /**
     * 修改用户
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    @Override
    @SaveOrUpdate
    public int updateSysUser(SysUser sysUser) {
        return sysUserMapper.updateSysUser(sysUser);
    }

    /**
     * 删除用户对象
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteSysUserById(String userId) {
        return sysUserMapper.deleteSysUserById(userId);
    }

    /**
     * 批量删除用户对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysUserByIds(Integer[] ids) {
        return sysUserMapper.deleteSysUserByIds(ids);
    }

}
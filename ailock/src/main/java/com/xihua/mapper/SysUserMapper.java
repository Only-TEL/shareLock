package com.xihua.mapper;

import com.xihua.bean.SysUser;
import java.util.List;

/**
 * 用户 数据层
 *
 * @author admin
 * @date 2019-12-03
 */
public interface SysUserMapper {
    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public SysUser selectSysUserById(String userId);

    /**
     * 查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户集合
     */
    public List<SysUser> selectSysUserList(SysUser sysUser);

    /**
     * 新增用户
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    public int insertSysUser(SysUser sysUser);

    /**
     * 修改用户
     *
     * @param sysUser 用户信息
     * @return 结果
     */
    public int updateSysUser(SysUser sysUser);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteSysUserById(String userId);

    /**
     * 批量删除用户
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysUserByIds(Integer[] userIds);

}
package com.xihua.mapper;

import com.xihua.bean.SysUser;
import java.util.List;
import com.xihua.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 数据层
 *
 * @author admin
 * @date 2019-12-04
 */
public interface SysUserMapper extends BaseMapper<SysUser, Integer> {

    SysUser selectUserByLoginName(@Param("userName") String userName);

    SysUser selectUserByPhoneNumber(@Param("phone") String phone);

    int checkLoginNameUnique(@Param("userName") String userName);

    int checkPhoneUnique(@Param("phone") String phone);

}
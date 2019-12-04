package com.xihua.controller;

import com.xihua.base.AjaxResult;
import com.xihua.base.BaseController;
import com.xihua.bean.SysUser;
import com.xihua.service.ISysUserService;
import com.xihua.utils.ServletUtil;
import com.xihua.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 用户信息展示controller
 */
@RestController
@RequestMapping("/lock/info")
public class SysInfoController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @PostMapping("/change")
    public AjaxResult changePhone(@RequestBody String json){
        SysUser changeUser = (SysUser) tranObject(json, SysUser.class);
        Integer userId = ServletUtil.getOnlineUser().getUserId();
        SysUser sysUser = sysUserService.selectById(userId);
        // 判断密码
        if(!sysUserService.validate(sysUser, changeUser.getPassword())){
            return AjaxResult.error("密码错误，请重试");
        }
        // 手机号相同
        if(sysUser.getPhone().equals(changeUser.getPhone())){
            return AjaxResult.error("该手机号已被注册");
        }
        // 验证手机号
        SysUser uniquePhone = sysUserService.selectUserByPhoneNumber(changeUser.getPhone());
        if(StringUtils.isNotEmpty(uniquePhone)){
            return AjaxResult.error("该手机号已被注册");
        }
        // 修改
        sysUser.setPhone(changeUser.getPhone());
        return toAjax(sysUserService.saveOrUpdateSysUser(sysUser));
    }
}

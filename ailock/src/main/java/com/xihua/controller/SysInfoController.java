package com.xihua.controller;

import com.xihua.base.AjaxResult;
import com.xihua.base.BaseController;
import com.xihua.bean.SysUser;
import com.xihua.service.IBackRecodService;
import com.xihua.service.ISysUserService;
import com.xihua.utils.ServletUtil;
import com.xihua.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 用户信息展示controller
 */
@Api("用户信息修改接口")
@RestController
@RequestMapping("/lock/info")
public class SysInfoController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private IBackRecodService backRecodService;

    // 改手机号
    @PostMapping("/change")
    @ApiOperation("修改绑定的手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonStr", value = "传入密码和新的手机号", required = true, paramType = "body", dataType = "String"),
    })
    public AjaxResult changePhone(@RequestBody String json){
        SysUser changeUser = (SysUser) tranObject(json, SysUser.class);
        Integer userId = ServletUtil.getOnlineUser().getUserId();
        SysUser sysUser = sysUserService.selectById(userId);
        // 判断密码
        if(!sysUserService.validate(sysUser, changeUser.getPassword())){
            return AjaxResult.error("密码错误，请重试");
        }
        // 验证手机号
        SysUser uniquePhone = sysUserService.selectUserByPhoneNumber(changeUser.getPhone());
        // 手机号相同
        if(sysUser.getPhone().equals(changeUser.getPhone()) || StringUtils.isNotEmpty(uniquePhone)){
            return AjaxResult.error("该手机号已被注册");
        }
        // 修改
        sysUser.setPhone(changeUser.getPhone());
        return toAjax(sysUserService.saveOrUpdateSysUser(sysUser));
    }
}

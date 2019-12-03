package com.xihua.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xihua.bean.SysUser;
import com.xihua.service.ISysUserService;
import com.xihua.base.AjaxResult;
import com.xihua.base.BaseController;

/**
 * 用户 信息操作处理
 *
 * @author admin
 * @date 2019-12-03
 */
@RestController
@RequestMapping("/xihua/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;



}
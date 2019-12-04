package com.xihua.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.xihua.bean.BackRecod;
import com.xihua.service.IBackRecodService;
import com.xihua.base.AjaxResult;
import com.xihua.base.BaseController;

/**
 * 行车记录 信息操作处理
 *
 * @author admin
 * @date 2019-12-04
 */
@RestController
@RequestMapping("/lock/backRecod")
public class BackRecodController extends BaseController {

    @Autowired
    private IBackRecodService backRecodService;



}
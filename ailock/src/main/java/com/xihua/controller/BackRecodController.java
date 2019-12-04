package com.xihua.controller;

import java.util.List;

import com.xihua.bean.SysUser;
import com.xihua.exception.BusinessException;
import com.xihua.utils.ServletUtil;
import com.xihua.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api("开锁接口")
@RestController
@RequestMapping("/lock/backRecod")
public class BackRecodController extends BaseController {

    @Autowired
    private IBackRecodService backRecodService;


    @ApiOperation("Http开锁接口")
    @PostMapping("/open")
    public AjaxResult open(@RequestBody String jsonStr){
        BackRecod openRecod = (BackRecod) tranObject(jsonStr, BackRecod.class);
        SysUser sysUser = ServletUtil.getOnlineUser();
        BackRecod exitRecord = backRecodService.selectByRunningRecord(sysUser.getUserId(),openRecod.getBackId());
        if(StringUtils.isNotEmpty(exitRecord)){
            throw new BusinessException("一人只能开锁一台单车，请检查开锁记录");
        }
        // 生成开锁记录
        return toAjax(backRecodService.buildOpenRecord(openRecod,sysUser));
    }

}
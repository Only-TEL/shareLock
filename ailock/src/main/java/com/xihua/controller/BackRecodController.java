package com.xihua.controller;

import java.util.List;

import com.xihua.bean.SysUser;
import com.xihua.exception.BusinessException;
import com.xihua.utils.ServletUtil;
import com.xihua.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonStr", value = "开锁的单车编号", required = true, paramType = "body", dataType = "String"),
    })
    @PostMapping("/open")
    public AjaxResult open(@RequestBody String jsonStr) {
        BackRecod openRecod = (BackRecod) tranObject(jsonStr, BackRecod.class);
        SysUser sysUser = ServletUtil.getOnlineUser();
        BackRecod exitRecord = backRecodService.selectByRunningRecord(sysUser.getUserId(), openRecod.getBackId());
        if (StringUtils.isNotEmpty(exitRecord)) {
            throw new BusinessException("一人只能开锁一台单车，请检查开锁记录");
        }
        // 生成开锁记录
        int row = backRecodService.buildOpenRecord(openRecod, sysUser);
        return row > 0 ? AjaxResult.success(sysUser.getPhone()) : AjaxResult.error("开锁失败");
        // todo 开锁成功后跳转至运行页面  在该页面使用websocket监听服务器的 /stop/back/{phone} 路径
    }

    @ApiOperation("获取已开锁的车辆信息")
    @GetMapping("/info")
    public AjaxResult openInfo(){
        BackRecod exitRecord = backRecodService.selectByRunningRecord(ServletUtil.getOnlineUserId());
        return AjaxResult.success(exitRecord);
    }

    @ApiOperation("获取所有的开锁的记录")
    @GetMapping("/all")
    public AjaxResult allInfo(){
        List<BackRecod> recodList = backRecodService.selectAllStopRecord(ServletUtil.getOnlineUserId());
        return AjaxResult.success(recodList);
    }

}
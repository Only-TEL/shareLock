package com.xihua.controller;


import com.xihua.constants.Constants;
import com.xihua.exception.BusinessException;
import com.xihua.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.xihua.bean.SysUser;
import com.xihua.service.ISysUserService;
import com.xihua.base.AjaxResult;
import com.xihua.base.BaseController;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * 用户 信息操作处理
 *
 * @author admin
 * @date 2019-12-04
 */
@RestController
@RequestMapping("/lock/user")
@Api(value = "Http用户接口")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 注册
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonStr", value = "注册表单json参数", required = true, paramType = "requestBody", dataType = "String"),
    })
    @PostMapping("/register")
    public AjaxResult register(@RequestBody String jsonStr) {
        SysUser sysUser = (SysUser) tranObject(jsonStr, SysUser.class);
        if (StringUtils.isEmpty(sysUser.getUserName()) || StringUtils.isEmpty(sysUser.getPassword())) {
            return AjaxResult.error("请输入用户名或密码进行注册");
        }
        boolean flag = sysUserService.register(sysUser);
        return flag ? AjaxResult.success() : AjaxResult.error("注册失败，请重试");
    }

    // 使用密码登录
    @ApiOperation("用户登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonStr", value = "登陆json参数,用户名+密码", required = true, paramType = "requestBody", dataType = "String"),
    })
    @PostMapping("/login")
    public AjaxResult login(@RequestBody String jsonStr) {
        SysUser loginUser = (SysUser) tranObject(jsonStr, SysUser.class);
        // 获取参数
        String userName = loginUser.getUserName();
        String password = loginUser.getPassword();
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(password)) {
            SysUser onlineUser = sysUserService.login(userName, password);
            // 将用户存储在session中
            onlineUser.setPassword("");
            HttpSession session = ServletUtil.getSession();
            String sessionId = session.getId();
            session.setAttribute(sessionId, onlineUser);
            //  将sessionId加密写入浏览器 cookie
           // System.out.println("oldSessionId=" + sessionId);
//            String newSessionId = EncryptUtil.encodeBase64(sessionId);
//            CookieUtils.setCookie(ServletUtil.getRequest(), ServletUtil.getResponse(),
//                    Constants.COOKIE_NAME, newSessionId, Constants.COOKIE_MAX_AGE, Constants.UTF8);
//            System.out.println("newSessionId=" + newSessionId);
            return AjaxResult.success();
        }
        return AjaxResult.error("用户或密码错误");
    }

    // 使用手机验证码登录
    @ApiOperation("手机号验证码登陆")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonStr", value = "登陆json参数，手机号+验证码", required = true, paramType = "requestBody", dataType = "String"),
    })
    @PostMapping("/plogin")
    public AjaxResult pLogin(@RequestBody String jsonStr) {
        SysUser loginUser = (SysUser) tranObject(jsonStr, SysUser.class);
        if(StringUtils.isEmpty(loginUser.getCaptcha())){
            throw new BusinessException("请输入验证码");
        }
        SysUser sysUser = sysUserService.selectUserByPhoneNumber(loginUser.getPhone());
        // 从redis中取验证码，过期返回重新获取验证码
        Object value = redisTemplate.opsForValue().get(sysUser.getUserId());
        if(StringUtils.isEmpty(value)){
           throw new BusinessException("验证码已过期，请重新获取验证码");
        }
        // 判断验证码是否一致
        if(!value.equals(loginUser.getCaptcha())){
            return AjaxResult.error("验证码错误，请重试");
        }
        return AjaxResult.success();
    }

    // 发送验证码
    @ApiOperation("获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path", dataType = "String"),
    })
    @GetMapping("/captcha/{phone}")
    public AjaxResult captcha(@PathVariable String phone) {
        SysUser sysUser = sysUserService.selectUserByPhoneNumber(phone);
        if (StringUtils.isEmpty(sysUser)) {
            return AjaxResult.error("请输入正确的手机号码");
        }
        String captcha = ZCommonUtils.buildCaptcha();
        // 存储在redis中10分钟 key=userId value=captcha
        redisTemplate.opsForValue().set(sysUser.getUserId(),captcha,10, TimeUnit.MINUTES);
        return AjaxResult.success(captcha);
    }
}
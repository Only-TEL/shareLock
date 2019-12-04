package com.xihua.config;

import com.xihua.bean.SysUser;
import com.xihua.constants.Constants;
import com.xihua.exception.BusinessException;
import com.xihua.utils.CookieUtils;
import com.xihua.utils.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 请求拦截器
 * @author: admin
 */
public class RequestUrlIntercepter extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前sessionId是否存在
        String value = CookieUtils.getCookieValue(request, Constants.DEFAULT_COOKIE_NAME);
        // SysUser sysUser = (SysUser) request.getSession().getAttribute(EncryptUtil.decodeBase64String(value));
        SysUser sysUser = (SysUser) request.getSession().getAttribute(value);
        if(StringUtils.isNotEmpty(sysUser)){
            return true;
        }
        // 暂时打开
        return true;
        // TODO 跳转到登陆页面
        //throw new BusinessException("请登录...");
    }
}

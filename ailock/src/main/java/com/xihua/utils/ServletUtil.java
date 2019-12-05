package com.xihua.utils;

import com.xihua.bean.SysUser;
import io.swagger.models.auth.In;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletUtil {

    public ServletUtil() {
    }

    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static String getSessionId() {
        return getRequest().getSession().getId();
    }

    public static SysUser getOnlineUser() {
        HttpSession session = getSession();
        return (SysUser) session.getAttribute(session.getId());
    }

    public static Integer getOnlineUserId() {
        return getOnlineUser().getUserId();
    }

    public static String getOnlineUserName() {
        SysUser sysUser = getOnlineUser();
        return sysUser.getUserName();
    }
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes)attributes;
    }

    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        return null;
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        } else {
            String xRequestedWith = request.getHeader("X-Requested-With");
            if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
                return true;
            } else {
                String uri = request.getRequestURI();
                if (StringUtils.inStringIgnoreCase(uri, new String[]{".json", ".xml"})) {
                    return true;
                } else {
                    String ajax = request.getParameter("__ajax");
                    return StringUtils.inStringIgnoreCase(ajax, new String[]{"json", "xml"});
                }
            }
        }
    }
}

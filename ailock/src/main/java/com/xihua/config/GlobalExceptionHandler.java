package com.xihua.config;

import com.xihua.base.AjaxResult;
import com.xihua.exception.BusinessException;
import com.xihua.utils.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler(){

    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public AjaxResult handleException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("不支持' " + e.getMethod() + "'请求");
    }

    @ExceptionHandler({RuntimeException.class})
    public AjaxResult notFount(RuntimeException e) {
        log.error("运行时异常:", e);
        return AjaxResult.error("运行时异常:" + e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public AjaxResult handleException(Exception e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error("服务器错误，请联系管理员");
    }

    @ExceptionHandler({BusinessException.class})
    public Object businessException(HttpServletRequest request, BusinessException e) {
        log.error(e.getMessage(), e);
        if (ServletUtil.isAjaxRequest(request)) {
            return AjaxResult.error(e.getMessage());
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("errorMessage", e.getMessage());
            modelAndView.setViewName("error/business");
            return modelAndView;
        }
    }
}

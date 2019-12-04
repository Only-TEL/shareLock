package com.xihua.exception;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected final String message;

    public BusinessException(String message) {
        System.out.println("异常信息为：" + message);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
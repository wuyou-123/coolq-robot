package com.wuyou.exception;

/**
 * JS未找到异常
 *
 * @author wuyou
 */
public class JavaScriptNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -6494098825180044350L;

    public JavaScriptNotFoundException() {
        super();
    }

    public JavaScriptNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public JavaScriptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JavaScriptNotFoundException(String message) {
        super(message);
    }

    public JavaScriptNotFoundException(Throwable cause) {
        super(cause);
    }

}

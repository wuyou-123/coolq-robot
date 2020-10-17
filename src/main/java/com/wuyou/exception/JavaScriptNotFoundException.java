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

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public JavaScriptNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * @param message
     * @param cause
     */
    public JavaScriptNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public JavaScriptNotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public JavaScriptNotFoundException(Throwable cause) {
        super(cause);
    }

}

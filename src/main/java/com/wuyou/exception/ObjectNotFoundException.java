package com.wuyou.exception;

/**
 * 对象未找到异常
 *
 * @author Administrator<br>
 * 2020年4月29日
 */
public class ObjectNotFoundException extends UpdateException {

    private static final long serialVersionUID = -6494098825180044350L;

    public ObjectNotFoundException() {
        super();
    }

    public ObjectNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }

}

package com.wuyou.exception;

/**
 * 对象已存在异常
 *
 * @author Administrator<br>
 * 2020年4月29日
 */
public class ObjectExistedException extends UpdateException {

    private static final long serialVersionUID = -3405292449079636375L;

    public ObjectExistedException() {
        super();
    }

    public ObjectExistedException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ObjectExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectExistedException(String message) {
        super(message);
    }

    public ObjectExistedException(Throwable cause) {
        super(cause);
    }

}

package com.wuyou.exception;

/**
 * 更新时异常
 *
 * @author Administrator<br>
 * 2020年4月29日
 */
public class UpdateException extends RuntimeException {
    private static final long serialVersionUID = 2031012135153356396L;

    public UpdateException() {
        super();
    }

    public UpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(Throwable cause) {
        super(cause);
    }

}

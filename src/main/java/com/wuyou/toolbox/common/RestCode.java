package com.wuyou.toolbox.common;

/**
 * @author wuyou
 */

public enum RestCode {
    /**
     * 返回的状态码和msg
     */
    OK(0, "ok"),
    ERROR(1,"未知异常"),
    //	NOT_LATEST(2, "不是最新版本"),
    VERSION_ALREADY_EXISTS(3, "此版本已存在"),
    ;
    public final int code;
    public final String msg;

    RestCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

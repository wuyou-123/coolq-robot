package com.wuyou.toolbox.common;

import lombok.Data;

/**
 * @author wuyou
 */
@Data
public class RestResponse<T> {
    private int code;
    private String msg;
    private T result;

    public RestResponse() {
        this(RestCode.OK.code, RestCode.OK.msg);
    }

    public RestResponse(int code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public static <T> RestResponse<T> success() {
        return new RestResponse<>();
    }

    public static <T> RestResponse<T> success(T result) {
        RestResponse<T> response = new RestResponse<>();
        response.setResult(result);
        return response;
    }

    public static <T> RestResponse<T> error(RestCode code) {
        return new RestResponse<>(code.code, code.msg);
    }

}

package com.lingzhi.core.exception;

import com.lingzhi.core.constant.Constants;
import lombok.Getter;

/**
 * 基础异常
 */
@Getter
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 错误数据
     */
    private final Object data;

    public BaseException(String message) {
        this(Constants.ERROR_CODE, message);
    }

    public BaseException(int code, String message) {
        this(code, message, null);
    }

    public BaseException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseException(String message, Object data) {
        this(Constants.BUSINESS_ERROR_CODE, message, data);
    }
}

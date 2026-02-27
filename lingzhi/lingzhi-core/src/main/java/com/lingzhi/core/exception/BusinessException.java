package com.lingzhi.core.exception;

import com.lingzhi.core.constant.Constants;

/**
 * 业务异常
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(Constants.BUSINESS_ERROR_CODE, message);
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(String message, Object data) {
        super(Constants.BUSINESS_ERROR_CODE, message, data);
    }

    public BusinessException(int code, String message, Object data) {
        super(code, message, data);
    }
}

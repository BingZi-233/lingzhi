package com.lingzhi.core.exception;

import com.lingzhi.core.constant.Constants;

/**
 * 资源未找到异常
 */
public class NotFoundException extends BaseException {

    private static final long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(Constants.NOT_FOUND_CODE, message);
    }

    public NotFoundException(String message, Object data) {
        super(Constants.NOT_FOUND_CODE, message, data);
    }
}

package com.lingzhi.common.result;

import com.lingzhi.core.constant.Constants;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 是否成功
     */
    private boolean success;

    public Result() {}

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = code == Constants.SUCCESS_CODE;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        return success(Constants.SUCCESS_MSG, data);
    }

    /**
     * 成功响应（带消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(Constants.SUCCESS_CODE, message, data);
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error() {
        return error(Constants.ERROR_MSG);
    }

    /**
     * 失败响应（带消息）
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(Constants.ERROR_CODE, message, null);
    }

    /**
     * 失败响应（带错误码和消息）
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败响应（带消息和数据）
     */
    public static <T> Result<T> error(String message, T data) {
        return new Result<>(Constants.ERROR_CODE, message, data);
    }

    /**
     * 自定义响应
     */
    public static <T> Result<T> of(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置请求ID
     */
    public Result<T> requestId(String requestId) {
        this.requestId = requestId;
        return this;
    }
}

package com.lingzhi.async.callback;

import java.util.function.Consumer;

/**
 * 异步任务回调
 */
public interface AsyncCallback {

    /**
     * 成功回调
     */
    default void onSuccess(Object result) {}

    /**
     * 失败回调
     */
    default void onFailure(Throwable throwable) {}

    /**
     * 完成回调
     */
    default void onComplete() {}

    /**
     * 使用函数式接口创建回调
     */
    static AsyncCallback of(Consumer<Object> success, Consumer<Throwable> failure) {
        return new AsyncCallback() {
            @Override
            public void onSuccess(Object result) {
                if (success != null) {
                    success.accept(result);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (failure != null) {
                    failure.accept(throwable);
                }
            }
        };
    }

    /**
     * 空回调
     */
    static AsyncCallback noop() {
        return new AsyncCallback() {};
    }
}

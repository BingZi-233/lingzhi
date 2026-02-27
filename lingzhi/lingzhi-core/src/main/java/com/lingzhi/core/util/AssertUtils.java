package com.lingzhi.core.util;

import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具
 */
public final class AssertUtils {

    private AssertUtils() {}

    /**
     * 断言表达式为真
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象不为空
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言对象为空
     */
    public static void isNull(Object obj, String message) {
        if (obj != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串不为空
     */
    public static void notEmpty(CharSequence str, String message) {
        if (StrUtil.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言字符串不为空（可去空格）
     */
    public static void notBlank(CharSequence str, String message) {
        if (StrUtil.isBlank(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言集合不为空
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数组不为空
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言Map不为空
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数字为正数
     */
    public static void isPositive(Number number, String message) {
        if (number == null || number.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言数字为非负数
     */
    public static void isNonNegative(Number number, String message) {
        if (number == null || number.doubleValue() < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言两个对象相等
     */
    public static void equals(Object o1, Object o2, String message) {
        if (o1 == null && o2 == null) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 断言条件，不满足则抛出自定义异常
     */
    public static <X extends RuntimeException> void isTrue(boolean expression, Supplier<X> exceptionSupplier) {
        if (!expression) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * 断言对象不为空，不满足则抛出自定义异常
     */
    public static <T, X extends RuntimeException> T notNull(T obj, Supplier<X> exceptionSupplier) {
        if (obj == null) {
            throw exceptionSupplier.get();
        }
        return obj;
    }

    /**
     * 断言字符串不为空，不满足则抛出自定义异常
     */
    public static <X extends RuntimeException> CharSequence notEmpty(CharSequence str, Supplier<X> exceptionSupplier) {
        if (StrUtil.isEmpty(str)) {
            throw exceptionSupplier.get();
        }
        return str;
    }

    /**
     * 断言集合不为空，不满足则抛出自定义异常
     */
    public static <T extends Collection<?>, X extends RuntimeException> T notEmpty(T collection, Supplier<X> exceptionSupplier) {
        if (collection == null || collection.isEmpty()) {
            throw exceptionSupplier.get();
        }
        return collection;
    }
}

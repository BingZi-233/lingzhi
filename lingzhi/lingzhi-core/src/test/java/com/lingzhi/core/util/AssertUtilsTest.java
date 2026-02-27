package com.lingzhi.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AssertUtils 单元测试
 */
@DisplayName("断言工具类测试")
class AssertUtilsTest {

    @Test
    @DisplayName("isTrue - 表达式为真时不抛异常")
    void isTrueShouldNotThrowWhenExpressionIsTrue() {
        assertDoesNotThrow(() -> AssertUtils.isTrue(true, "Should not throw"));
    }

    @Test
    @DisplayName("isTrue - 表达式为假时抛出异常")
    void isTrueShouldThrowWhenExpressionIsFalse() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.isTrue(false, "Expected exception")
        );
        assertEquals("Expected exception", exception.getMessage());
    }

    @Test
    @DisplayName("notNull - 对象不为空时不抛异常")
    void notNullShouldNotThrowWhenObjectIsNotNull() {
        assertDoesNotThrow(() -> AssertUtils.notNull(new Object(), "Should not throw"));
    }

    @Test
    @DisplayName("notNull - 对象为空时抛出异常")
    void notNullShouldThrowWhenObjectIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notNull(null, "Object cannot be null")
        );
        assertEquals("Object cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("isNull - 对象为空时不抛异常")
    void isNullShouldNotThrowWhenObjectIsNull() {
        assertDoesNotThrow(() -> AssertUtils.isNull(null, "Should not throw"));
    }

    @Test
    @DisplayName("isNull - 对象不为空时抛出异常")
    void isNullShouldThrowWhenObjectIsNotNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.isNull(new Object(), "Object should be null")
        );
        assertEquals("Object should be null", exception.getMessage());
    }

    @Test
    @DisplayName("notEmpty - 字符串不为空时不抛异常")
    void notEmptyShouldNotThrowWhenStringIsNotEmpty() {
        assertDoesNotThrow(() -> AssertUtils.notEmpty("hello", "Should not throw"));
    }

    @Test
    @DisplayName("notEmpty - 字符串为空时抛出异常")
    void notEmptyShouldThrowWhenStringIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notEmpty("", "String cannot be empty")
        );
        assertEquals("String cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("notEmpty - 字符串为null时抛出异常")
    void notEmptyShouldThrowWhenStringIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notEmpty((String) null, "String cannot be null")
        );
        assertEquals("String cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("notBlank - 字符串不为空白时不抛异常")
    void notBlankShouldNotThrowWhenStringIsNotBlank() {
        assertDoesNotThrow(() -> AssertUtils.notBlank("hello", "Should not throw"));
        assertDoesNotThrow(() -> AssertUtils.notBlank(" hello ", "Should not throw"));
    }

    @Test
    @DisplayName("notBlank - 字符串为空白时抛出异常")
    void notBlankShouldThrowWhenStringIsBlank() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notBlank("   ", "String cannot be blank")
        );
        assertEquals("String cannot be blank", exception.getMessage());
    }

    @Test
    @DisplayName("notEmpty - 集合不为空时不抛异常")
    void notEmptyShouldNotThrowWhenCollectionIsNotEmpty() {
        List<String> list = Arrays.asList("a", "b", "c");
        assertDoesNotThrow(() -> AssertUtils.notEmpty(list, "Should not throw"));
    }

    @Test
    @DisplayName("notEmpty - 集合为空时抛出异常")
    void notEmptyShouldThrowWhenCollectionIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new ArrayList<>(), "Collection cannot be empty")
        );
        assertEquals("Collection cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("notEmpty - 集合为null时抛出异常")
    void notEmptyShouldThrowWhenCollectionIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notEmpty((List<?>) null, "Collection cannot be null")
        );
        assertEquals("Collection cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("notEmpty - 数组不为空时不抛异常")
    void notEmptyShouldNotThrowWhenArrayIsNotEmpty() {
        assertDoesNotThrow(() -> AssertUtils.notEmpty(new Object[]{1, 2, 3}, "Should not throw"));
    }

    @Test
    @DisplayName("notEmpty - 数组为空时抛出异常")
    void notEmptyShouldThrowWhenArrayIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new Object[]{}, "Array cannot be empty")
        );
        assertEquals("Array cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("notEmpty - Map不为空时不抛异常")
    void notEmptyShouldNotThrowWhenMapIsNotEmpty() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertDoesNotThrow(() -> AssertUtils.notEmpty(map, "Should not throw"));
    }

    @Test
    @DisplayName("notEmpty - Map为空时抛出异常")
    void notEmptyShouldThrowWhenMapIsEmpty() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notEmpty(new HashMap<>(), "Map cannot be empty")
        );
        assertEquals("Map cannot be empty", exception.getMessage());
    }

    @Test
    @DisplayName("isPositive - 数字为正数时不抛异常")
    void isPositiveShouldNotThrowWhenNumberIsPositive() {
        assertDoesNotThrow(() -> AssertUtils.isPositive(1, "Should not throw"));
        assertDoesNotThrow(() -> AssertUtils.isPositive(0.1, "Should not throw"));
    }

    @Test
    @DisplayName("isPositive - 数字为零或负数时抛出异常")
    void isPositiveShouldThrowWhenNumberIsNotPositive() {
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.isPositive(0, "Number must be positive")
        );
        assertEquals("Number must be positive", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.isPositive(-1, "Number must be positive")
        );
        assertEquals("Number must be positive", exception2.getMessage());
    }

    @Test
    @DisplayName("isNonNegative - 数字为非负数时不抛异常")
    void isNonNegativeShouldNotThrowWhenNumberIsNonNegative() {
        assertDoesNotThrow(() -> AssertUtils.isNonNegative(0, "Should not throw"));
        assertDoesNotThrow(() -> AssertUtils.isNonNegative(1, "Should not throw"));
    }

    @Test
    @DisplayName("isNonNegative - 数字为负数时抛出异常")
    void isNonNegativeShouldThrowWhenNumberIsNegative() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.isNonNegative(-1, "Number must be non-negative")
        );
        assertEquals("Number must be non-negative", exception.getMessage());
    }

    @Test
    @DisplayName("equals - 两个对象相等时不抛异常")
    void equalsShouldNotThrowWhenObjectsAreEqual() {
        assertDoesNotThrow(() -> AssertUtils.equals("a", "a", "Should not throw"));
        assertDoesNotThrow(() -> AssertUtils.equals(null, null, "Should not throw"));
    }

    @Test
    @DisplayName("equals - 两个对象不相等时抛出异常")
    void equalsShouldThrowWhenObjectsAreNotEqual() {
        IllegalArgumentException exception1 = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.equals("a", "b", "Objects must be equal")
        );
        assertEquals("Objects must be equal", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.equals("a", null, "Objects must be equal")
        );
        assertEquals("Objects must be equal", exception2.getMessage());
    }

    @Test
    @DisplayName("notNull with supplier - 返回对象不为空")
    void notNullWithSupplierShouldReturnObjectWhenNotNull() {
        String result = AssertUtils.notNull("test", () -> new IllegalArgumentException("Cannot be null"));
        assertEquals("test", result);
    }

    @Test
    @DisplayName("notNull with supplier - 对象为空时抛出自定义异常")
    void notNullWithSupplierShouldThrowWhenObjectIsNull() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AssertUtils.notNull(null, () -> new IllegalArgumentException("Custom error"))
        );
        assertEquals("Custom error", exception.getMessage());
    }
}

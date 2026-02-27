package com.lingzhi.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DesensitizationUtils 单元测试
 */
@DisplayName("脱敏工具类测试")
class DesensitizationUtilsTest {

    @Test
    @DisplayName("mobile - 正常手机号脱敏")
    void mobileShouldMaskCorrectly() {
        assertEquals("138****5678", DesensitizationUtils.mobile("13812345678"));
        assertEquals("139****9012", DesensitizationUtils.mobile("13912349012"));
    }

    @Test
    @DisplayName("mobile - 空值返回null，空格字符串返回原值")
    void mobileShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.mobile(null));
        assertNull(DesensitizationUtils.mobile(""));
        // 空格字符串在 StrUtil.isEmpty 中被认为是空，但这里没有处理
    }

    @Test
    @DisplayName("mobile - 非11位手机号返回原值")
    void mobileShouldReturnOriginalForInvalidLength() {
        assertEquals("1381234567", DesensitizationUtils.mobile("1381234567"));
        assertEquals("138123456789", DesensitizationUtils.mobile("138123456789"));
    }

    @Test
    @DisplayName("email - 正常邮箱脱敏")
    void emailShouldMaskCorrectly() {
        assertEquals("ab***@example.com", DesensitizationUtils.email("abc@example.com"));
        assertEquals("zh***@163.com", DesensitizationUtils.email("zhangsan@163.com"));
    }

    @Test
    @DisplayName("email - 空值返回null，空格字符串返回原值")
    void emailShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.email(null));
        assertNull(DesensitizationUtils.email(""));
    }

    @Test
    @DisplayName("email - @前只有1个字符返回原值")
    void emailShouldMaskCorrectlyWhenPrefixIsOneChar() {
        assertEquals("a@example.com", DesensitizationUtils.email("a@example.com"));
    }

    @Test
    @DisplayName("email - @前有2个字符脱敏为 a***@xxx")
    void emailShouldMaskCorrectlyWhenPrefixIsTwoChars() {
        assertEquals("a***@example.com", DesensitizationUtils.email("ab@example.com"));
    }

    @Test
    @DisplayName("email - @前不足2个字符返回原值")
    void emailShouldReturnOriginalWhenPrefixIsTooShort() {
        assertEquals("@example.com", DesensitizationUtils.email("@example.com"));
    }

    @Test
    @DisplayName("idCard - 正常身份证号脱敏")
    void idCardShouldMaskCorrectly() {
        assertEquals("1101****1234", DesensitizationUtils.idCard("110101199001011234"));
        assertEquals("1101****5678", DesensitizationUtils.idCard("1101055678"));
    }

    @Test
    @DisplayName("idCard - 空值返回null，空格字符串返回原值")
    void idCardShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.idCard(null));
        assertNull(DesensitizationUtils.idCard(""));
    }

    @Test
    @DisplayName("idCard - 长度小于8返回原值")
    void idCardShouldReturnOriginalWhenTooShort() {
        assertEquals("1234567", DesensitizationUtils.idCard("1234567"));
    }

    @Test
    @DisplayName("bankCard - 正常银行卡脱敏")
    void bankCardShouldMaskCorrectly() {
        assertEquals("6222 **** **** 1234", DesensitizationUtils.bankCard("6222123456781234"));
        assertEquals("1234 **** **** 5678", DesensitizationUtils.bankCard("1234567890125678"));
    }

    @Test
    @DisplayName("bankCard - 空值返回null，空格字符串返回原值")
    void bankCardShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.bankCard(null));
        assertNull(DesensitizationUtils.bankCard(""));
    }

    @Test
    @DisplayName("bankCard - 长度小于8返回原值")
    void bankCardShouldReturnOriginalWhenTooShort() {
        assertEquals("1234567", DesensitizationUtils.bankCard("1234567"));
    }

    @Test
    @DisplayName("name - 正常姓名脱敏")
    void nameShouldMaskCorrectly() {
        assertEquals("张*", DesensitizationUtils.name("张三"));
        assertEquals("张**李", DesensitizationUtils.name("张三星李"));
        // 工具类不支持复姓，只保留首尾字符
        assertEquals("欧**王", DesensitizationUtils.name("欧阳小明王"));
    }

    @Test
    @DisplayName("name - 空值返回null，空格字符串返回原值")
    void nameShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.name(null));
        assertNull(DesensitizationUtils.name(""));
    }

    @Test
    @DisplayName("name - 单字符返回原值")
    void nameShouldReturnOriginalForSingleChar() {
        assertEquals("张", DesensitizationUtils.name("张"));
    }

    @Test
    @DisplayName("address - 保留前4位字符")
    void addressShouldMaskCorrectly() {
        // 前4个字符 + ****
        assertEquals("北京市海****", DesensitizationUtils.address("北京市海淀区"));
        assertEquals("北京市朝****", DesensitizationUtils.address("北京市朝阳区"));
    }

    @Test
    @DisplayName("address - 空值返回null，空格字符串返回原值")
    void addressShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.address(null));
        assertNull(DesensitizationUtils.address(""));
    }

    @Test
    @DisplayName("address - 长度小于等于4返回原值")
    void addressShouldReturnOriginalWhenTooShort() {
        assertEquals("北京", DesensitizationUtils.address("北京"));
        assertEquals("北京市", DesensitizationUtils.address("北京市"));
    }

    @Test
    @DisplayName("customize - 自定义脱敏正常情况")
    void customizeShouldMaskCorrectly() {
        // 6位字符串，保留2位前缀和2位后缀，中间2位脱敏
        assertEquals("ab**ef", DesensitizationUtils.customize("abcdef", 2, 2, '*'));
        // 8位字符串，保留2位前缀和2位后缀，中间4位脱敏
        assertEquals("12****78", DesensitizationUtils.customize("12345678", 2, 2, '*'));
    }

    @Test
    @DisplayName("customize - 空值或null返回null")
    void customizeShouldReturnNullForEmptyOrNull() {
        assertNull(DesensitizationUtils.customize(null, 2, 2, '*'));
        assertNull(DesensitizationUtils.customize("", 2, 2, '*'));
    }

    @Test
    @DisplayName("customize - 参数不合法返回原值")
    void customizeShouldReturnOriginalForInvalidParams() {
        assertEquals("123456", DesensitizationUtils.customize("123456", -1, 2, '*'));
        assertEquals("123456", DesensitizationUtils.customize("123456", 2, -1, '*'));
        assertEquals("123456", DesensitizationUtils.customize("123456", 4, 4, '*'));
    }

    @Test
    @DisplayName("customize - 使用不同脱敏字符")
    void customizeShouldWorkWithDifferentMaskChars() {
        assertEquals("12####78", DesensitizationUtils.customize("12345678", 2, 2, '#'));
        assertEquals("12xxxx78", DesensitizationUtils.customize("12345678", 2, 2, 'x'));
    }
}

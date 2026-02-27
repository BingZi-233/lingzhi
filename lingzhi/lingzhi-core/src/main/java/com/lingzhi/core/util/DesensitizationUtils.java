package com.lingzhi.core.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

/**
 * 脱敏工具
 */
public final class DesensitizationUtils {

    private DesensitizationUtils() {}

    /**
     * 手机号脱敏 (138****5678)
     */
    public static String mobile(String mobile) {
        if (StrUtil.isEmpty(mobile)) {
            return null;
        }
        if (mobile.length() != 11) {
            return mobile;
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 邮箱脱敏 (a***@example.com)
     */
    public static String email(String email) {
        if (StrUtil.isEmpty(email)) {
            return null;
        }
        int atIndex = email.indexOf("@");
        if (atIndex <= 1) {
            return email;
        }
        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);
        if (prefix.length() <= 2) {
            return prefix.charAt(0) + "***" + suffix;
        }
        return prefix.substring(0, 2) + "***" + suffix;
    }

    /**
     * 身份证号脱敏 (110101****12345678)
     */
    public static String idCard(String idCard) {
        if (StrUtil.isEmpty(idCard)) {
            return null;
        }
        if (idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "****" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 银行卡脱敏 (6222 **** **** 1234)
     */
    public static String bankCard(String bankCard) {
        if (StrUtil.isEmpty(bankCard)) {
            return null;
        }
        if (bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + " **** **** " + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 姓名脱敏 (张*)
     */
    public static String name(String name) {
        if (StrUtil.isEmpty(name)) {
            return null;
        }
        if (name.length() == 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        return name.charAt(0) + "**" + name.charAt(name.length() - 1);
    }

    /**
     * 地址脱敏 (北京市海淀区****)
     */
    public static String address(String address) {
        if (StrUtil.isEmpty(address)) {
            return null;
        }
        if (address.length() <= 4) {
            return address;
        }
        return address.substring(0, 4) + "****";
    }

    /**
     * 自定义脱敏
     *
     * @param str   原始字符串
     * @param prefix 前保留位数
     * @param suffix 后保留位数
     * @param mask   脱敏字符
     */
    public static String customize(String str, int prefix, int suffix, char mask) {
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        if (prefix < 0 || suffix < 0 || prefix + suffix > str.length()) {
            return str;
        }
        int maskLength = str.length() - prefix - suffix;
        if (maskLength <= 0) {
            return str;
        }
        char[] chars = new char[maskLength];
        Arrays.fill(chars, mask);
        return str.substring(0, prefix) + new String(chars) + str.substring(str.length() - suffix);
    }
}

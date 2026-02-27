package com.lingzhi.monitor.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.lingzhi.monitor.annotation.Sensitive;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 敏感数据序列化器
 */
public class SensitiveSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // 获取字段上的 @Sensitive 注解
        try {
            Field field = getField(gen);
            if (field != null) {
                Sensitive annotation = field.getAnnotation(Sensitive.class);
                if (annotation != null) {
                    gen.writeString(desensitize(value, annotation.type()));
                    return;
                }
            }
        } catch (Exception e) {
            // 忽略
        }

        gen.writeString(value);
    }

    private Field getField(JsonGenerator gen) {
        try {
            return gen.getOutputContext().getCurrentName() != null ? 
                gen.getCurrentValue().getClass().getDeclaredField(gen.getOutputContext().getCurrentName()) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 脱敏处理
     */
    private String desensitize(String value, Sensitive.SensitiveType type) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        switch (type) {
            case NAME:
                return desensitizeName(value);
            case PHONE:
                return desensitizePhone(value);
            case EMAIL:
                return desensitizeEmail(value);
            case ID_CARD:
                return desensitizeIdCard(value);
            case BANK_CARD:
                return desensitizeBankCard(value);
            case ADDRESS:
                return desensitizeAddress(value);
            case NONE:
            default:
                return value;
        }
    }

    /**
     * 姓名脱敏：张*丰
     */
    private String desensitizeName(String name) {
        if (name.length() <= 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.charAt(0) + "*";
        }
        return name.charAt(0) + "*" + name.charAt(name.length() - 1);
    }

    /**
     * 手机号脱敏：138****1234
     */
    private String desensitizePhone(String phone) {
        if (phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 邮箱脱敏：a***@example.com
     */
    private String desensitizeEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return email;
        }
        String prefix = email.substring(0, atIndex);
        String suffix = email.substring(atIndex);
        
        if (prefix.length() <= 1) {
            return "*" + suffix;
        }
        return prefix.charAt(0) + "***" + suffix;
    }

    /**
     * 身份证脱敏：3201***********1234
     */
    private String desensitizeIdCard(String idCard) {
        if (idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "***********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 银行卡脱敏：6228***********1234
     */
    private String desensitizeBankCard(String bankCard) {
        if (bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "***********" + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 地址脱敏：江苏省南京市***
     */
    private String desensitizeAddress(String address) {
        int length = address.length();
        if (length <= 6) {
            return address;
        }
        return address.substring(0, length - 3) + "***";
    }
}

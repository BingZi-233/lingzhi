# lingzhi-captcha 验证码模块

> 图片验证码

## 模块简介

`lingzhi-captcha` 提供验证码能力：

- **@CaptchaGenerate** - 生成验证码
- **@CaptchaValidate** - 校验验证码
- **图片验证码** - 字符验证码

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-captcha</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方式

### @CaptchaGenerate - 生成验证码

```java
@CaptchaGenerate(key = "#request.phone", type = "sms")
@GetMapping("/captcha")
public Result<CaptchaResult> generate(@RequestBody CaptchaRequest request) {
    // 返回验证码结果
    return Result.success(captchaResult);
}
```

### @CaptchaValidate - 校验验证码

```java
@CaptchaValidate(key = "#phone")
@PostMapping("/verify")
public Result<Void> verify(@RequestParam String phone, @RequestParam String code) {
    return Result.success();
}
```

## 注解属性

### @CaptchaGenerate

| 属性 | 说明 | 默认值 |
|------|------|--------|
| key | 验证码 key，支持 SpEL | - |
| type | 验证码类型：image/sms/email | image |
| expire | 过期时间(秒) | 300 |
| length | 长度 | 4 |

### @CaptchaValidate

| 属性 | 说明 | 默认值 |
|------|------|--------|
| key | 验证码 key，支持 SpEL | - |
| code | 验证码参数名 | code |
| required | 是否必需 | true |
| message | 错误消息 | 验证码错误 |

## 依赖

- lingzhi-core
- captcha

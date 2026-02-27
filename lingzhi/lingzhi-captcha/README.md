# lingzhi-captcha 验证码模块

> 图片验证码

## 模块简介

`lingzhi-captcha` 提供验证码能力：

- **图片验证码** - 字符验证码
- **算术验证码** - 简单算术题
- **滑动验证码** - 滑动拼图

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

### 生成验证码

```java
@Service
public class CaptchaService {

    @Autowired
    private CaptchaService captchaService;

    public CaptchaResult generate() {
        // 生成验证码图片
        CaptchaImageIO image = captchaService.getImage();
        
        return new CaptchaResult(
            image.getImageBase64(),  // Base64 图片
            image.getCacheKey()      // 缓存 key
        );
    }
}
```

### 验证验证码

```java
public boolean verify(String cacheKey, String code) {
    return captchaService.verify(cacheKey, code);
}
```

## 依赖

- lingzhi-core
- captcha

# lingzhi-core 核心基础模块

> 核心基础 - 常量、工具类、异常、枚举

## 模块简介

`lingzhi-core` 是灵芝框架的核心基础模块，提供：

- **常量定义** - 统一响应码、系统常量
- **工具类** - 通用工具方法
- **异常类** - 业务异常、 系统异常
- **枚举** - 常用枚举定义

## 常量

### 响应码

```java
public final class Constants {
    // 成功/失败
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 500;
    
    // 认证相关
    public static final int UNAUTHORIZED_CODE = 401;
    public static final int FORBIDDEN_CODE = 403;
    
    // 资源相关
    public static final int NOT_FOUND_CODE = 404;
    public static final int BAD_REQUEST_CODE = 400;
    
    // 业务异常
    public static final int BUSINESS_ERROR_CODE = 1000;
}
```

### 分页常量

```java
public final class Constants {
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 1000;
}
```

### 日期格式

```java
public final class Constants {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
}
```

## 异常类

### 业务异常

```java
throw new ServiceException("业务处理失败");
// 或带错误码
throw new ServiceException(1001, "业务处理失败");
```

### 参数校验异常

```java
throw new IllegalArgumentException("参数不能为空");
```

### 资源未找到异常

```java
throw new ResourceNotFoundException("用户不存在");
```

## 使用方式

```java
import com.lingzhi.core.constant.Constants;
import com.lingzhi.core.exception.ServiceException;

@Service
public class UserService {

    public User getUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }
        return user;
    }
}
```

## 依赖

无独立依赖，被其他所有模块引用

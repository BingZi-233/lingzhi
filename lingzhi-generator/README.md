# lingzhi-generator 代码生成模块

> MyBatis Plus 代码生成

## 模块简介

`lingzhi-generator` 提供代码生成能力：

- **Entity** - 实体类生成
- **Mapper** - Mapper 接口生成
- **Service** - Service 层生成
- **Controller** - Controller 生成

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-generator</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 使用

```java
public static void main(String[] args) {
    FastAutoGenerator.create(url, user, password)
        .globalConfig(builder -> {
            builder.author("lingzhi")
                   .outputDir("src/main/java");
        })
        .packageConfig(builder -> {
            builder.parent("com.lingzhi")
                   .moduleName("demo");
        })
        .strategyConfig(builder -> {
            builder.addInclude("sys_user");
        })
        .execute();
}
```

## 配置项

| 配置 | 说明 |
|------|------|
| globalConfig | 全局配置 |
| packageConfig | 包配置 |
| strategyConfig | 策略配置 |
| templateConfig | 模板配置 |
| injectionConfig | 注入配置 |

## 依赖

- lingzhi-core
- mybatis-plus-generator
- freemarker

# lingzhi-db 数据库模块

> MyBatis Plus、分布式ID、乐观锁

## 模块简介

`lingzhi-db` 提供数据库相关能力：

- **MyBatis Plus** - 增强的 MyBatis
- **多数据源** - 动态数据源切换
- **分布式ID** - 雪花算法 ID 生成
- **乐观锁** - 字段版本控制

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-db</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
spring:
  datasource:
    dynamic:
      primary: master
      strict: false
      datasource:
        master:
          url: jdbc:mysql://localhost:3306/test
          username: root
          password: root
        slave:
          url: jdbc:mysql://localhost:3306/test_slave
          username: root
          password: root
```

## 使用方式

### Entity

```java
@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String username;
    private String password;
    private String email;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
```

### Mapper

```java
public interface UserMapper extends BaseMapper<User> {
    // 自定义方法
}
```

### Service

```java
@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    public User getUserById(Long id) {
        return getById(id);
    }
    
    public List<User> listByUsername(String username) {
        return lambdaQuery()
            .eq(User::getUsername, username)
            .list();
    }
    
    public boolean saveUser(User user) {
        return save(user);
    }
    
    public boolean updateUser(User user) {
        return updateById(user);
    }
    
    public boolean deleteUser(Long id) {
        return removeById(id);
    }
}
```

## 分布式ID

### 配置

```yaml
lingzhi:
  db:
    id:
      worker-id: 1
      datacenter-id: 1
```

### 使用

```java
// 自动配置，ID 会在插入时自动生成
userMapper.insert(user);

// 或手动获取
long id = IdUtils.getId();
```

## 乐观锁

### 配置

```java
@Version
private Integer version;
```

### 使用

```java
// 更新时会自动检查 version
User user = getById(1);
user.setUsername("newName");
updateById(user);  // 如果 version 变化，会抛出异常
```

## 多数据源

### 切换数据源

```java
// 使用 @DS 注解
@DS("slave")
public List<User> listFromSlave() {
    return list();
}

// 手动切换
DynamicDataSourceContextHolder.push("slave");
try {
    return list();
} finally {
    DynamicDataSourceContextHolder.clear();
}
```

## 配置项

```yaml
lingzhi:
  db:
    id:
      worker-id: 1          # 工作机器ID (0-31)
      datacenter-id: 1     # 数据中心ID (0-31)
```

## 依赖

- lingzhi-core
- mybatis-plus-spring-boot3-starter
- dynamic-datasource-spring-boot3-starter

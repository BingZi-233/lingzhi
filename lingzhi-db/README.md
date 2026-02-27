# lingzhi-db 数据库模块

> MyBatis Plus、分布式ID、乐观锁、多数据源

## 模块简介

`lingzhi-db` 提供数据库相关能力：

- **MyBatis Plus** - 增强的 MyBatis
- **多数据源** - 动态数据源切换
- **分布式ID** - 雪花算法 ID 生成
- **乐观锁** - 字段版本控制
- **自动填充** - 创建时间、更新时间等

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
          driver-class-name: com.mysql.cj.jdbc.Driver
        slave:
          url: jdbc:mysql://localhost:3306/test_slave
          username: root
          password: root
```

## 使用方式

### 定义实体

```java
@Data
@TableName("sys_user")
public class SysUser extends BaseEntity {
    
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    
    @Version
    private Integer version;
}
```

### 定义 Mapper

```java
public interface SysUserMapper extends BaseMapper<SysUser> {
}
```

### 定义 Service

```java
public interface SysUserService extends BaseService<SysUser> {
    
    SysUser getByUsername(String username);
    
    boolean updatePassword(Long userId, String oldPassword, String newPassword);
}

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> 
    implements SysUserService {
    
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUsername, username));
    }
    
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        SysUser user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (!PasswordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(PasswordEncoder.encode(newPassword));
        return updateById(user);
    }
}
```

### 基础 CRUD

```java
@Autowired
private SysUserService userService;

// 查询
SysUser user = userService.getById(1L);
List<SysUser> list = userService.list();
IPage<SysUser> page = userService.page(new Page<>(1, 10));

// 新增
SysUser user = new SysUser();
user.setUsername("admin");
userService.save(user);

// 修改
user.setNickname("管理员");
userService.updateById(user);

// 删除（逻辑删除）
userService.removeById(1L);
```

### 条件查询

```java
// 简单条件
List<SysUser> users = userService.list(
    new LambdaQueryWrapper<SysUser>()
        .eq(SysUser::getStatus, 1)
        .like(SysUser::getUsername, "admin")
        .orderByDesc(SysUser::getCreateTime)
);

// 分页查询
IPage<SysUser> page = userService.page(
    new Page<>(1, 10),
    new LambdaQueryWrapper<SysUser>()
        .eq(SysUser::getStatus, 1)
);
```

## 分布式ID

### 配置

```yaml
lingzhi:
  db:
    id:
      enabled: true
      worker-id: 1
      datacenter-id: 1
```

### 使用

```java
// ID 自动生成，插入时无需设置
SysUser user = new SysUser();
user.setUsername("admin");
userService.save(user);  // id 自动生成

// 手动获取
long id = IdUtil.getId();
```

## 乐观锁

```java
// 实体定义
@Version
private Integer version;

// 更新时自动检查版本
SysUser user = getById(1L);
user.setNickname("新名字");
updateById(user);  // 如果 version 变化，抛出异常
```

## 多数据源

### 切换数据源

```java
// 注解方式
@DS("slave")
public List<SysUser> listFromSlave() {
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
      enabled: true           # 是否启用分布式ID
      worker-id: 1          # 工作机器ID (0-31)
      datacenter-id: 1      # 数据中心ID (0-31)

# MyBatis Plus 配置
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.lingzhi.**.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      id-type: assign_id
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

## 依赖

- lingzhi-core
- mybatis-plus-spring-boot3-starter
- dynamic-datasource-spring-boot3-starter
- mysql-connector-j

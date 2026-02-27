# lingzhi-excel Excel 模块

> EasyExcel 导入导出

## 模块简介

`lingzhi-excel` 提供 Excel 处理能力：

- **导入** - Excel 数据导入
- **导出** - 数据导出为 Excel
- **模板** - 模板填充

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-excel</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 使用方式

### 导出

```java
@Service
public class UserExportService {

    public void export(HttpServletResponse response) {
        List<User> users = userService.list();
        
        // 导出
        EasyExcel.write(response.getOutputStream())
            .head(User.class)
            .sheet("用户列表")
            .doWrite(users);
    }
}
```

### 导入

```java
@PostMapping("/import")
public Result<Void> importExcel(MultipartFile file) {
    EasyExcel.read(file.getInputStream())
        .head(User.class)
        .sheet()
        .doRead();
    
    return Result.success();
}
```

### 监听器

```java
public class UserImportListener extends AnalysisEventListener<User> {
    
    private List<User> list = new ArrayList<>();
    
    @Override
    public void invoke(User user, AnalysisContext context) {
        list.add(user);
        if (list.size() >= 100) {
            save(list);
            list.clear();
        }
    }
    
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!list.isEmpty()) {
            save(list);
        }
    }
}
```

## 依赖

- lingzhi-core
- easyexcel

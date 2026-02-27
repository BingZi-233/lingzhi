# lingzhi-file 文件处理模块

> 本地存储、MinIO、阿里云OSS

## 模块简介

`lingzhi-file` 提供文件处理能力：

- **本地存储** - 本地文件上传下载
- **MinIO** - 对象存储
- **阿里云 OSS** - 云存储
- **文件类型** - 图片、PDF 等

## 快速开始

### 1. 引入依赖

```xml
<dependency>
    <groupId>com.lingzhi</groupId>
    <artifactId>lingzhi-file</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. 配置

```yaml
lingzhi:
  file:
    type: local
    local:
      path: /data/uploads
      domain: http://localhost:8080
```

## 使用方式

### 上传文件

```java
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        String url = fileService.upload(file);
        return Result.success(url);
    }
}
```

### 下载文件

```java
@GetMapping("/download/{filename}")
public void download(@PathVariable String filename, HttpServletResponse response) {
    fileService.download(filename, response);
}
```

## 存储类型

### 本地存储

```yaml
lingzhi:
  file:
    type: local
    local:
      path: /data/uploads
      domain: http://localhost:8080
```

### MinIO

```yaml
lingzhi:
  file:
    type: minio
    minio:
      endpoint: http://localhost:9000
      access-key: minioadmin
      secret-key: minioadmin
      bucket-name: lingzhi
```

### 阿里云 OSS

```yaml
lingzhi:
  file:
    type: oss
    oss:
      endpoint: oss-cn-hangzhou.aliyuncs.com
      access-key: xxx
      secret-key: xxx
      bucket-name: lingzhi
```

## 依赖

- lingzhi-core
- spring-boot-starter-web
- minio
- aliyun-oss-java-sdk

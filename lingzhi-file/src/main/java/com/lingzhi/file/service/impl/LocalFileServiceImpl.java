package com.lingzhi.file.service.impl;

import com.lingzhi.file.config.FileProperties;
import com.lingzhi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 本地文件存储服务
 */
@Slf4j
@Service("localFileService")
@RequiredArgsConstructor
public class LocalFileServiceImpl implements FileService {

    private final FileProperties fileProperties;

    @Override
    public String upload(MultipartFile file) {
        return upload(file, generateFileName(file.getOriginalFilename()));
    }

    @Override
    public String upload(MultipartFile file, String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return upload(file, datePath, fileName);
    }

    @Override
    public String upload(MultipartFile file, String directory, String fileName) {
        try {
            String basePath = fileProperties.getLocal().getPath();
            String fullPath = basePath + "/" + directory;
            
            // 创建目录
            Path path = Paths.get(fullPath);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // 生成唯一文件名
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "") 
                + getExtension(fileName);
            Path filePath = path.resolve(uniqueFileName);

            // 保存文件
            Files.copy(file.getInputStream(), filePath);

            // 返回访问 URL
            String domain = fileProperties.getLocal().getDomain();
            return domain + "/" + directory + "/" + uniqueFileName;
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String fileUrl) {
        try {
            String path = fileUrl.replace(fileProperties.getLocal().getDomain(), "");
            Path filePath = Paths.get(fileProperties.getLocal().getPath() + path);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("文件删除失败: {}", fileUrl, e);
            return false;
        }
    }

    @Override
    public InputStream getInputStream(String fileUrl) {
        try {
            String path = fileUrl.replace(fileProperties.getLocal().getDomain(), "");
            Path filePath = Paths.get(fileProperties.getLocal().getPath() + path);
            return new FileInputStream(filePath.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("文件不存在: " + fileUrl, e);
        }
    }

    @Override
    public String getUrl(String path) {
        return fileProperties.getLocal().getDomain() + "/" + path;
    }

    @Override
    public boolean exists(String fileUrl) {
        String path = fileUrl.replace(fileProperties.getLocal().getDomain(), "");
        return Files.exists(Paths.get(fileProperties.getLocal().getPath() + path));
    }

    /**
     * 生成文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = getExtension(originalFilename);
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }

    /**
     * 获取文件扩展名
     */
    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}

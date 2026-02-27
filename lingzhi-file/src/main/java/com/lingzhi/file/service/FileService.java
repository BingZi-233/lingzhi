package com.lingzhi.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 文件存储服务接口
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 文件访问 URL
     */
    String upload(MultipartFile file);

    /**
     * 上传文件（指定文件名）
     */
    String upload(MultipartFile file, String fileName);

    /**
     * 上传文件（指定路径）
     */
    String upload(MultipartFile file, String directory, String fileName);

    /**
     * 删除文件
     */
    boolean delete(String fileUrl);

    /**
     * 获取文件流
     */
    InputStream getInputStream(String fileUrl);

    /**
     * 获取文件访问 URL
     */
    String getUrl(String path);

    /**
     * 判断文件是否存在
     */
    boolean exists(String fileUrl);
}

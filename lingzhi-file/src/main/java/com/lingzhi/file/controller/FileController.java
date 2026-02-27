package com.lingzhi.file.controller;

import com.lingzhi.common.result.Result;
import com.lingzhi.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件控制器
 */
@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "文件不能为空");
        }
        
        try {
            String url = fileService.upload(file);
            return Result.success(url);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping
    public Result<Void> delete(@RequestParam String fileUrl) {
        boolean success = fileService.delete(fileUrl);
        return success ? Result.success() : Result.error("文件删除失败");
    }

    /**
     * 下载文件
     */
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(@RequestParam String fileUrl) {
        try {
            InputStream inputStream = fileService.getInputStream(fileUrl);
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            log.error("文件下载失败: {}", fileUrl, e);
            return ResponseEntity.notFound().build();
        }
    }
}

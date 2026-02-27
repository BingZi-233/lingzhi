package com.lingzhi.excel.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Excel 服务
 */
public interface ExcelService {

    /**
     * 读取 Excel（所有数据）
     */
    <T> List<T> read(MultipartFile file, Class<T> clazz);

    /**
     * 读取 Excel（带监听器）
     */
    <T> void read(MultipartFile file, Class<T> clazz, ReadListener<T> listener);

    /**
     * 导出 Excel（单个sheet）
     */
    <T> void write(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName);

    /**
     * 导出 Excel（多个sheet）
     */
    void write(HttpServletResponse response, List<SheetData> sheets);

    /**
     * Sheet 数据
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    class SheetData {
        private String sheetName;
        private List<?> data;
        private Class<?> clazz;
    }
}

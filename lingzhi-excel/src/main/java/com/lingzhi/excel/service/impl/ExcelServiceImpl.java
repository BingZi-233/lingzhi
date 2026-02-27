package com.lingzhi.excel.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.lingzhi.excel.service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Excel 服务实现
 */
@Slf4j
@Service
public class ExcelServiceImpl implements ExcelService {

    @Override
    public <T> List<T> read(MultipartFile file, Class<T> clazz) {
        try {
            // 使用 EasyExcel 提供的工具类
            return EasyExcel.read(file.getInputStream())
                .head(clazz)
                .sheet()
                .doReadSync();
        } catch (IOException e) {
            log.error("读取Excel失败", e);
            throw new RuntimeException("读取Excel失败: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> void read(MultipartFile file, Class<T> clazz, ReadListener<T> listener) {
        try {
            EasyExcel.read(file.getInputStream(), clazz, listener).sheet().doRead();
        } catch (IOException e) {
            log.error("读取Excel失败", e);
            throw new RuntimeException("读取Excel失败: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> void write(HttpServletResponse response, List<T> data, Class<T> clazz, String fileName) {
        try {
            OutputStream outputStream = getOutputStream(response, fileName);
            ExcelWriter writer = EasyExcel.write(outputStream, clazz).build();
            WriteSheet sheet = EasyExcel.writerSheet("Sheet1").build();
            writer.write(data, sheet);
            writer.finish();
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("导出Excel失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void write(HttpServletResponse response, List<SheetData> sheets) {
        try {
            OutputStream outputStream = getOutputStream(response, "export");
            ExcelWriter writer = EasyExcel.write(outputStream).build();
            
            for (int i = 0; i < sheets.size(); i++) {
                SheetData sheetData = sheets.get(i);
                WriteSheet sheet = EasyExcel.writerSheet(i, sheetData.getSheetName())
                    .head((Class) sheetData.getClazz())
                    .build();
                writer.write((List) sheetData.getData(), sheet);
            }
            
            writer.finish();
        } catch (IOException e) {
            log.error("导出Excel失败", e);
            throw new RuntimeException("导出Excel失败: " + e.getMessage(), e);
        }
    }

    private OutputStream getOutputStream(HttpServletResponse response, String fileName) throws IOException {
        String name = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");
        return response.getOutputStream();
    }
}

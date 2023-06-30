package com.enjplatform.reconciliation.loader;

import com.enjplatform.reconciliation.before.ResourceLoader;
import com.enjplatform.reconciliation.domain.CheckEntry;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

/**
 * @ClassName ExcelResourceLoader
 * @Description 文件资源读取器 一行一行读取,针对单行文件
 * @Author starslink
 * @Date 2023/5/30 17:23
 * @Version 1.0
 */
public class FileResourceLoader<T> implements ResourceLoader {

    /**
     * 文件读取配置
     */
    private FileResourceConfig fileResourceConfig;

    /**
     * 文件资源读取器
     *
     * @param fileResourceConfig
     */
    public FileResourceLoader(FileResourceConfig fileResourceConfig) {
        this.fileResourceConfig = fileResourceConfig;
    }

    /**
     * 文件资源配置类
     *
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileResourceConfig<T> {

        /**
         * 文件路径名format
         */
        private String filePathFormat;
        /**
         * 主键id
         */
        private String identityField;
        /**
         * 检查字段
         */
        private List<String> checkFields;
        /**
         * 转换类型
         */
        private FileEntityParser<T> fileEntityParser;
    }

    public String getFilePath(String date) {
        return String.format(fileResourceConfig.getFilePathFormat(), date);
    }

    @Override
    public List<CheckEntry> load(String date) {
        List<T> list = readFile(getFilePath(date));
        return CheckEntry.wrap(list, fileResourceConfig.getIdentityField(), fileResourceConfig.getCheckFields());
    }

    @SneakyThrows
    public List<T> readFile(String filePath) {
        FileEntityParser<T> fileEntityParser = fileResourceConfig.getFileEntityParser();
        return Files.readAllLines(Paths.get(filePath)).stream().map(v -> fileEntityParser.parse(v)).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface FileEntityParser<T> {

        T parse(String data);
    }
}

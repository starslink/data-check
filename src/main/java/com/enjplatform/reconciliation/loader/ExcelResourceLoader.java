package com.enjplatform.reconciliation.loader;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ExcelResourceLoader
 * @Description excel文件读取器
 * @Author starslink
 * @Date 2023/5/30 17:23
 * @Version 1.0
 */
public class ExcelResourceLoader<T> extends FileResourceLoader {

    private Class<T> clazz;

    /**
     * excel 配置类
     *
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExcelResourceConfig<T> extends FileResourceConfig {

        /**
         * 泛型类
         */
        private Class<T> clazz;
    }

    /**
     * Excel文件资源读取器
     *
     * @param excelResourceConfig
     */
    public ExcelResourceLoader(ExcelResourceConfig excelResourceConfig) {
        super(excelResourceConfig);
        this.clazz = excelResourceConfig.getClazz();
    }

    @Override
    public List<T> readFile(String filePath) {
        List<T> result = new ArrayList<>(100);

        EasyExcel.read(filePath, this.clazz, new ReadListener<T>() {
            @Override
            public void invoke(T data, AnalysisContext context) {
                result.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {

            }
        }).sheet().doRead();
        return result;
    }
}

package com.enjplatform.reconciliation.before;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.enjplatform.reconciliation.domain.CheckEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ResourceReaderAdapter
 * @Description 资源加载适配器
 * @Author starslink
 * @Date 2023/6/5 19:39
 * @Version 1.0
 */
public abstract class ResourceReaderAdapter implements ResourceReader {

    /**
     * 开始时间
     */
    protected String beginDate;
    /**
     * 结束时间
     */
    protected String endDate;

    /**
     * 设置时间信息
     *
     * @param date
     */
    protected void initDate(String date) {
        LocalDate currentDate = LocalDateTimeUtil.parseDate(date, DatePattern.NORM_DATE_FORMATTER);
        // 日期加1天
        LocalDate localDate = currentDate.plusDays(1);
        // 增加时间查询
        this.beginDate = LocalDateTimeUtil.format(currentDate, DatePattern.NORM_DATE_FORMATTER);
        this.endDate = LocalDateTimeUtil.format(localDate, DatePattern.NORM_DATE_FORMATTER);
    }

    /**
     * 转换查询结果
     *
     * @param sourceMaps
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> List<CheckEntry> convertCheckEntry(List<Map> sourceMaps, Class<T> clazz) {

        List<T> clazzList = JSONUtil.toList(JSONUtil.toJsonStr(sourceMaps), clazz);

        List<CheckEntry> checkEntries = CheckEntry.wrap(clazzList);

        return checkEntries;
    }

    @Override
    public ResourceLoader getDifferentLoader() {
        return null;
    }
}

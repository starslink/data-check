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
 * @Description ��Դ����������
 * @Author starslink
 * @Date 2023/6/5 19:39
 * @Version 1.0
 */
public abstract class ResourceReaderAdapter implements ResourceReader {

    /**
     * ��ʼʱ��
     */
    protected String beginDate;
    /**
     * ����ʱ��
     */
    protected String endDate;

    /**
     * ����ʱ����Ϣ
     *
     * @param date
     */
    protected void initDate(String date) {
        LocalDate currentDate = LocalDateTimeUtil.parseDate(date, DatePattern.NORM_DATE_FORMATTER);
        // ���ڼ�1��
        LocalDate localDate = currentDate.plusDays(1);
        // ����ʱ���ѯ
        this.beginDate = LocalDateTimeUtil.format(currentDate, DatePattern.NORM_DATE_FORMATTER);
        this.endDate = LocalDateTimeUtil.format(localDate, DatePattern.NORM_DATE_FORMATTER);
    }

    /**
     * ת����ѯ���
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

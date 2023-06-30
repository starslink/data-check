package com.enjplatform.reconciliation.domain;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.enjplatform.reconciliation.annotation.CheckField;
import com.enjplatform.reconciliation.annotation.CheckIdentity;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.experimental.Tolerate;


/**
 * ����ʵ���װ
 *
 * @author starslink
 */
@Data
//@Builder
public class CheckEntry {

    /**
     * һ�Զ�����:ҵ����������� group by ֧�ֶ���ֶε�ƴ��,�Զ���method {@link CheckAdapter#getKey()}
     */
    private String key;
    /**
     * ʵ��
     */
    private Object entity;

    /**
     * ����Դͷ����
     */
    private Map<String, Object> additionalSourceFields;

    /**
     * ����Ŀ������
     */
    private Map<String, Object> additionalTargetFields;

    /**
     * keyΪ�Աȹؼ��� valueΪ�Ա�����ֵ
     */
    private Map<String, Object> checkData;

    @Tolerate
    public CheckEntry() {
        //do-nothing
    }

    public CheckEntry(CheckAdapter checkAdapter) {
        this.key = checkAdapter.getKey();
        this.entity = checkAdapter;
        this.checkData = checkAdapter.getCheckData();
        this.additionalSourceFields = checkAdapter.additionalSourceFields();
        this.additionalTargetFields = checkAdapter.additionalTargetFields();
    }

    public CheckEntry(String key, Object entity, Map<String, Object> checkData) {
        this.key = key;
        this.entity = entity;
        this.checkData = checkData;
    }

    /**
     * �Զ��崫����Ҫ�Ƚϵĵ�Ԫ
     * <p>
     * ��adpter������ʽ
     *
     * @param entity      �Ա�ʵ��
     * @param keyField    �Ա�Ψһ��ʶ
     * @param checkFields ��Ҫ�Աȵ�������
     * @return
     */
    public static CheckEntry wrap(Object entity, String keyField, List<String> checkFields) {
        if (CharSequenceUtil.isEmpty(keyField)) {
            Optional<String> optional = Arrays.stream(entity.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(CheckIdentity.class) != null)
                    .map(Field::getName).findFirst();
            keyField = optional.orElseThrow(() -> new RuntimeException("�Ƚ����ʶ������"));
        }
        if (checkFields == null || checkFields.isEmpty()) {
            checkFields = Arrays.stream(entity.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(CheckField.class) != null)
                    .sorted(Comparator.comparingInt(f -> f.getAnnotation(CheckField.class).order())).map(Field::getName).collect(Collectors.toList());
        }
        String identity = (String) ReflectUtil.getFieldValue(entity, keyField);
        Map<String, Object> map = new HashMap<>();
        for (String field : checkFields) {
            map.put(field, ReflectUtil.getFieldValue(entity, field));
        }
        return new CheckEntry(identity, entity, map);
    }

    /**
     * װ��ȶԶ��� ����˳��: adapter > annotaion
     *
     * @param entity �Ա�ʵ��
     * @return
     */
    public static CheckEntry wrap(Object entity) {
        if (entity instanceof CheckAdapter) {
            CheckAdapter adapter = (CheckAdapter) entity;
            return new CheckEntry(adapter);
        }
        return wrap(entity, null, null);
    }

    /**
     * ����״̬�ȶԶ���
     *
     * @param entityList  �Ա�ʵ��list
     * @param keyField    �Ա�ʵ��Ψһ��ʶ
     * @param checkFields ��Ҫ�Աȵ�������
     * @return
     */
    public static List<CheckEntry> wrap(List<?> entityList, String keyField, List<String> checkFields) {
        return entityList.stream().map(v -> wrap(v, keyField, checkFields)).collect(Collectors.toList());
    }

    /**
     * ����״̬�ȶԶ���
     *
     * @param entityList �Ա�ʵ��list
     * @return
     */
    public static List<CheckEntry> wrap(List<?> entityList) {
        return entityList.stream().map(CheckEntry::wrap).collect(Collectors.toList());
    }
}

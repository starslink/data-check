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
 * 对账实体包装
 *
 * @author starslink
 */
@Data
//@Builder
public class CheckEntry {

    /**
     * 一对多的情况:业务方在外层自行 group by 支持多个字段的拼接,自定义method {@link CheckAdapter#getKey()}
     */
    private String key;
    /**
     * 实体
     */
    private Object entity;

    /**
     * 附加源头数据
     */
    private Map<String, Object> additionalSourceFields;

    /**
     * 附加目标数据
     */
    private Map<String, Object> additionalTargetFields;

    /**
     * key为对比关键字 value为对比属性值
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
     * 自定义传入需要比较的单元
     * <p>
     * 非adpter适配形式
     *
     * @param entity      对比实体
     * @param keyField    对比唯一标识
     * @param checkFields 需要对比的属性名
     * @return
     */
    public static CheckEntry wrap(Object entity, String keyField, List<String> checkFields) {
        if (CharSequenceUtil.isEmpty(keyField)) {
            Optional<String> optional = Arrays.stream(entity.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(CheckIdentity.class) != null)
                    .map(Field::getName).findFirst();
            keyField = optional.orElseThrow(() -> new RuntimeException("比较类标识不存在"));
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
     * 装配比对对象 优先顺序: adapter > annotaion
     *
     * @param entity 对比实体
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
     * 批量状态比对对象
     *
     * @param entityList  对比实体list
     * @param keyField    对比实体唯一标识
     * @param checkFields 需要对比的属性名
     * @return
     */
    public static List<CheckEntry> wrap(List<?> entityList, String keyField, List<String> checkFields) {
        return entityList.stream().map(v -> wrap(v, keyField, checkFields)).collect(Collectors.toList());
    }

    /**
     * 批量状态比对对象
     *
     * @param entityList 对比实体list
     * @return
     */
    public static List<CheckEntry> wrap(List<?> entityList) {
        return entityList.stream().map(CheckEntry::wrap).collect(Collectors.toList());
    }
}

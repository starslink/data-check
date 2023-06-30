package com.enjplatform.reconciliation.domain;

import cn.hutool.core.map.MapUtil;
import java.util.Map;

/**
 * @author starslink E CheckEntry 的包装类,为了解决对比实体关系复杂的问题(1.唯一标识存在多个字段拼接,2.两个不同类中的对比字段的值不一致,比如A类中的"1"要和B类中的"b"识别为一致)
 */
public interface CheckAdapter {

    /**
     * 唯一标识,可能存在多个字段拼接
     */
    String getKey();

    /**
     * 比较数据
     */
    Map<String, Object> getCheckData();

    /**
     * 附加源头数据,不需要对比的数据，但是结果需要的数据
     *
     * @return
     */
    default Map<String, Object> additionalSourceFields() {
        return MapUtil.empty();
    }

    /**
     * 附加目标数据,不需要对比的数据，但是结果需要的数据
     *
     * @return
     */
    default Map<String, Object> additionalTargetFields() {
        return MapUtil.empty();
    }

}

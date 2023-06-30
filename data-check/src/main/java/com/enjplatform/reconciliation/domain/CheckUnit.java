package com.enjplatform.reconciliation.domain;

import com.enjplatform.reconciliation.enums.CheckStateEnum;
import com.enjplatform.reconciliation.enums.CheckSyncEnum;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 每个实体的比较单元
 *
 * @author starslink
 */
@Data
@Builder
public class CheckUnit {

    /**
     * 比较单元的唯一标识
     */
    private String key;

    /**
     * 差异属性 key: 属性名 val: 差异细节: source:xxx target:xxx
     */
    private Map<String, FieldUnit> differentFields;

    /**
     * 附加源头数据
     */
    private Map<String, Object> additionalSourceFields;

    /**
     * 附加目标数据
     */
    private Map<String, Object> additionalTargetFields;

    /**
     * 对账状态
     */
    private CheckStateEnum state;

    /**
     * 调账状态
     */
    private CheckSyncEnum sync;

    @Tolerate
    public CheckUnit() {
        //do-nothing
    }

    @Override
    public String toString() {
        return "key='" + key + "', state=" + state + ", differentFields=" + differentFields;
    }
}

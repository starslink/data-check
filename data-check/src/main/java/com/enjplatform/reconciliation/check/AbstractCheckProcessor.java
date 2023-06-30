package com.enjplatform.reconciliation.check;


import com.enjplatform.reconciliation.domain.CheckContext;
import com.enjplatform.reconciliation.domain.CheckEntry;
import com.enjplatform.reconciliation.domain.CheckUnit;
import com.enjplatform.reconciliation.domain.FieldUnit;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import com.enjplatform.reconciliation.enums.CheckStateEnum;
import com.enjplatform.reconciliation.enums.CheckSyncEnum;
import com.enjplatform.reconciliation.enums.ExecutorStatusEnum;
import com.enjplatform.reconciliation.executor.ExecutorManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author starslink
 */
public abstract class AbstractCheckProcessor implements CheckProcessor {

    public void compareCheck(CheckContext context) {
        List<CheckEntry> source = context.getSource();
        List<CheckEntry> target = context.getTarget();
        //都为空时,继续走
        if (source.isEmpty() && target.isEmpty()) {
            return;
        }
        //单个为空时,认为数据没准备好
        if (source.isEmpty() || target.isEmpty()) {
            return;
//            throw new CheckException("对账数据未准备好");
        }
    }

    /**
     * 比较具体实现
     *
     * @param context
     */
    public abstract void doCompare(CheckContext context);

    /**
     * 设置缓存方式
     *
     * @return
     */
    public abstract CheckCacheEnum checkCache();

    @Override
    public void compare(CheckContext context) {
        ExecutorManager executorManager = context.getExecutorManager();
        executorManager.setStatus(ExecutorStatusEnum.CHECK);

        compareCheck(context);
        doCompare(context);
    }

    /**
     * 添加附加属性
     *
     * @param checkUnits
     * @param sourceMap
     * @param targetMap
     */
    protected void additionalFields(List<CheckUnit> checkUnits, Map<String, CheckEntry> sourceMap, Map<String, CheckEntry> targetMap) {
        checkUnits.forEach(checkUnit -> {
            String key = checkUnit.getKey();
            if (sourceMap.containsKey(key)) {
                CheckEntry checkEntry = sourceMap.get(key);
                checkUnit.setAdditionalSourceFields(checkEntry.getAdditionalSourceFields());
            }

            if (targetMap.containsKey(key)) {
                CheckEntry checkEntry = targetMap.get(key);
                checkUnit.setAdditionalTargetFields(checkEntry.getAdditionalTargetFields());
            }
        });
    }

    /**
     * 比较两个Map数据的不同
     *
     * @param sourceMap           源数据
     * @param targetMap           目标数据
     * @param allowableErrorRange 允许误差值
     * @return
     */
    protected List<CheckUnit> compareMap(Map<String, CheckEntry> sourceMap, Map<String, CheckEntry> targetMap, BigDecimal allowableErrorRange) {
        List<CheckUnit> unitDetails = new ArrayList<>();
        sourceMap.forEach((k, v) -> {
            if (!targetMap.containsKey(k)) {
                unitDetails.add(CheckUnit.builder()
                        .key(k)
                        .state(CheckStateEnum.SOURCE_MORE)
                        .sync(CheckSyncEnum.NO_SYNC)
                        .differentFields(createSingle(v.getCheckData(), true))
                        .build());
                return;
            }
            Map<String, FieldUnit> fieldUnitMap = compareCheckData(v.getCheckData(), targetMap.get(k).getCheckData(), allowableErrorRange);
            if (fieldUnitMap == null || fieldUnitMap.isEmpty()) {
                return;
            }
            unitDetails.add(CheckUnit.builder()
                    .key(k)
                    .state(CheckStateEnum.DIFFERENT)
                    .sync(CheckSyncEnum.NO_SYNC)
                    .differentFields(fieldUnitMap)
                    .build());
        });
        targetMap.forEach((k, v) -> {
            if (!sourceMap.containsKey(k)) {
                unitDetails.add(CheckUnit.builder()
                        .key(k)
                        .state(CheckStateEnum.TARGET_MORE)
                        .sync(CheckSyncEnum.NO_SYNC)
                        .differentFields(createSingle(v.getCheckData(), false))
                        .build());
            }

        });
        return unitDetails;
    }

    /**
     * 比较两个 checkData 的详细属性不同
     *
     * @param sourceData          来源数据
     * @param targetData          目标数据
     * @param allowableErrorRange 可允许误差范围 绝对值
     * @return
     */
    public Map<String, FieldUnit> compareCheckData(Map<String, Object> sourceData, Map<String, Object> targetData, BigDecimal allowableErrorRange) {

        Map<String, FieldUnit> different = new HashMap<>();

        sourceData.forEach((k, v) -> {
            Object targetValue = targetData.get(k);
            if (targetData.containsKey(k) && v.equals(targetValue)) {
                return; // 值相等, 跳过
            }

            if (allowableErrorRange != null && v instanceof BigDecimal && targetValue instanceof BigDecimal) {
                BigDecimal source = (BigDecimal) v;
                BigDecimal target = (BigDecimal) targetValue;

                if (allowErrorRange(source, target, allowableErrorRange)) {
                    return; // 在误差范围内,跳过
                }
            }
            different.put(k, FieldUnit.builder().fieldName(k).source(v).target(targetValue).build());
        });
        targetData.forEach((k, v) -> {
            if (sourceData.containsKey(k)) {
                return;
            }
            different.put(k, FieldUnit.builder().fieldName(k).target(v).build());
        });
        return different;
    }

    /**
     * 比较是否在误差范围内
     *
     * @param value               当前值
     * @param target              目标值
     * @param allowableErrorRange 可允许误差范围
     * @return true 允许 false 不允许
     */
    public boolean allowErrorRange(BigDecimal value, BigDecimal target, BigDecimal allowableErrorRange) {
        BigDecimal upperBound = target.add(allowableErrorRange);
        BigDecimal lowerBound = target.subtract(allowableErrorRange);
        return value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) <= 0;
    }

    /**
     * source_more 与 target_more 的情况下,将checkData映射为fieldkUnitMap 放入checkUnit 方便使用拿到原始值, 方判断是否需要处理
     *
     * @param checkData 原始值
     * @param isSource  数据来源: true:源, false:目标
     * @return
     */
    public Map<String, FieldUnit> createSingle(Map<String, Object> checkData, boolean isSource) {
        Map<String, FieldUnit> different = new HashMap<>();
        checkData.forEach((k, v) -> {
            if (isSource) {
                different.put(k, FieldUnit.builder().fieldName(k).source(v).build());
                return;
            }
            different.put(k, FieldUnit.builder().fieldName(k).target(v).build());
        });
        return different;
    }
}

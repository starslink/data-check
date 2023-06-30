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
        //��Ϊ��ʱ,������
        if (source.isEmpty() && target.isEmpty()) {
            return;
        }
        //����Ϊ��ʱ,��Ϊ����û׼����
        if (source.isEmpty() || target.isEmpty()) {
            return;
//            throw new CheckException("��������δ׼����");
        }
    }

    /**
     * �ȽϾ���ʵ��
     *
     * @param context
     */
    public abstract void doCompare(CheckContext context);

    /**
     * ���û��淽ʽ
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
     * ��Ӹ�������
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
     * �Ƚ�����Map���ݵĲ�ͬ
     *
     * @param sourceMap           Դ����
     * @param targetMap           Ŀ������
     * @param allowableErrorRange �������ֵ
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
     * �Ƚ����� checkData ����ϸ���Բ�ͬ
     *
     * @param sourceData          ��Դ����
     * @param targetData          Ŀ������
     * @param allowableErrorRange ��������Χ ����ֵ
     * @return
     */
    public Map<String, FieldUnit> compareCheckData(Map<String, Object> sourceData, Map<String, Object> targetData, BigDecimal allowableErrorRange) {

        Map<String, FieldUnit> different = new HashMap<>();

        sourceData.forEach((k, v) -> {
            Object targetValue = targetData.get(k);
            if (targetData.containsKey(k) && v.equals(targetValue)) {
                return; // ֵ���, ����
            }

            if (allowableErrorRange != null && v instanceof BigDecimal && targetValue instanceof BigDecimal) {
                BigDecimal source = (BigDecimal) v;
                BigDecimal target = (BigDecimal) targetValue;

                if (allowErrorRange(source, target, allowableErrorRange)) {
                    return; // ����Χ��,����
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
     * �Ƚ��Ƿ�����Χ��
     *
     * @param value               ��ǰֵ
     * @param target              Ŀ��ֵ
     * @param allowableErrorRange ��������Χ
     * @return true ���� false ������
     */
    public boolean allowErrorRange(BigDecimal value, BigDecimal target, BigDecimal allowableErrorRange) {
        BigDecimal upperBound = target.add(allowableErrorRange);
        BigDecimal lowerBound = target.subtract(allowableErrorRange);
        return value.compareTo(lowerBound) >= 0 && value.compareTo(upperBound) <= 0;
    }

    /**
     * source_more �� target_more �������,��checkDataӳ��ΪfieldkUnitMap ����checkUnit ����ʹ���õ�ԭʼֵ, ���ж��Ƿ���Ҫ����
     *
     * @param checkData ԭʼֵ
     * @param isSource  ������Դ: true:Դ, false:Ŀ��
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

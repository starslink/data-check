package com.enjplatform.reconciliation.before;

import com.enjplatform.reconciliation.domain.CheckContext;
import com.enjplatform.reconciliation.domain.CheckEntry;
import com.enjplatform.reconciliation.domain.CheckUnit;
import com.enjplatform.reconciliation.domain.FieldUnit;
import com.enjplatform.reconciliation.enums.CheckStateEnum;
import com.enjplatform.reconciliation.enums.CheckSyncEnum;
import com.enjplatform.reconciliation.enums.ExecutorStatusEnum;
import com.enjplatform.reconciliation.executor.ExecutorManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author starslink
 */
public abstract class AbstractBeforeCheckProcessor implements BeforeCheckProcessor, CheckPre, ResourceReader {

    public List<CheckEntry> getSource(String date) {
        return getSourceLoader().load(date);
    }

    public List<CheckEntry> getTarget(String date) {
        return getTargetLoader().load(date);
    }

    public List<CheckEntry> getDifferent(String date) {
        ResourceLoader differentLoader = getDifferentLoader();
        if (differentLoader != null) {
            return differentLoader.load(date);
        }
        return Collections.emptyList();
    }


    @Override
    public Boolean processBeforeCompare(CheckContext context) {
        ExecutorManager executorManager = context.getExecutorManager();
        executorManager.setStatus(ExecutorStatusEnum.BEFORE);
        String date = context.getDate();
        //前置校验不通过
        if (!doPre(context)) {
            return false;
        }
        List<CheckEntry> different = getDifferent(date);
        if (different != null) {
            //todo 差异数据处理,重复对比
        }
        List<CheckEntry> source = getSource(date);
        List<CheckEntry> target = getTarget(date);

        List<CheckUnit> checkUnits = checkEntryRepeat(source, target);
        context.setPreDetails(checkUnits);

        Map<String, CheckEntry> sourceMap = source.stream().collect(Collectors.toMap(CheckEntry::getKey, Function.identity()));
        Map<String, CheckEntry> targetMap = target.stream().collect(Collectors.toMap(CheckEntry::getKey, Function.identity()));
        context.setSource(source);
        context.setTarget(target);
        context.setSourceMap(sourceMap);
        context.setTargetMap(targetMap);
        return true;
    }

    /**
     * key 不唯一重复数据处理
     *
     * @param source
     * @param target
     */
    private List<CheckUnit> checkEntryRepeat(List<CheckEntry> source, List<CheckEntry> target) {

        Map<String, Long> countSourceMap = source.stream().collect(Collectors.groupingBy(CheckEntry::getKey, Collectors.counting()));
        Map<String, Long> countTargetMap = target.stream().collect(Collectors.groupingBy(CheckEntry::getKey, Collectors.counting()));

        Iterator<CheckEntry> sourceIterator = source.iterator();
        Iterator<CheckEntry> targetIterator = target.iterator();

        List<CheckUnit> preCheckUnits = new ArrayList<>(10);

        while (sourceIterator.hasNext()) {
            CheckEntry checkEntry = sourceIterator.next();
            // 重复Key
            String key = checkEntry.getKey();
            if (countSourceMap.getOrDefault(key, 0L) > 1) {
                CheckUnit checkUnit = CheckUnit.builder()
                        .key(key)
                        .additionalSourceFields(checkEntry.getAdditionalSourceFields())
                        .additionalTargetFields(checkEntry.getAdditionalTargetFields())
                        .state(CheckStateEnum.SOURCE_REPEAT)
                        .sync(CheckSyncEnum.NO_SYNC)
                        .differentFields(createSingle(checkEntry.getCheckData(), true))
                        .build();
                preCheckUnits.add(checkUnit);
                sourceIterator.remove();
            }
        }

        while (targetIterator.hasNext()) {
            CheckEntry checkEntry = targetIterator.next();
            // 重复Key
            String key = checkEntry.getKey();
            if (countTargetMap.getOrDefault(key, 0L) > 1) {
                CheckUnit checkUnit = CheckUnit.builder()
                        .key(key)
                        .additionalSourceFields(checkEntry.getAdditionalSourceFields())
                        .additionalTargetFields(checkEntry.getAdditionalTargetFields())
                        .state(CheckStateEnum.TARGET_REPEAT)
                        .sync(CheckSyncEnum.NO_SYNC)
                        .differentFields(createSingle(checkEntry.getCheckData(), false))
                        .build();
                preCheckUnits.add(checkUnit);
                targetIterator.remove();
            }
        }

        return preCheckUnits;
    }

    /**
     * 创建重复数据
     *
     * @param checkData
     * @param isSource
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

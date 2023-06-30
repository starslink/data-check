package com.enjplatform.reconciliation.check;

import com.enjplatform.reconciliation.domain.CheckContext;
import com.enjplatform.reconciliation.domain.CheckEntry;
import com.enjplatform.reconciliation.domain.CheckResult;
import com.enjplatform.reconciliation.domain.CheckUnit;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import com.enjplatform.reconciliation.enums.CheckStateEnum;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author starslink
 */
public class MemoryCheckProcessor extends AbstractCheckProcessor {

    @Override
    public void doCompare(CheckContext context) {
        Map<String, CheckEntry> sourceMap = context.getSourceMap();
        Map<String, CheckEntry> targetMap = context.getTargetMap();
        List<CheckUnit> unitDetails = compareMap(sourceMap, targetMap, context.getAllowableErrorRange());
        additionalFields(unitDetails, sourceMap, targetMap);
        // 对账前置重复数据过滤
        List<CheckUnit> preDetails = context.getPreDetails();
        if (CollectionUtils.isNotEmpty(preDetails)) {
            unitDetails.addAll(preDetails);
        }
        Map<CheckStateEnum, List<CheckUnit>> unitDetailsMap = unitDetails.stream().collect(Collectors.groupingBy(CheckUnit::getState));
        Map<String, CheckEntry> sourceDiffMap = Optional.ofNullable(unitDetailsMap.get(CheckStateEnum.SOURCE_MORE)).orElse(Collections.emptyList())
                .stream().collect(Collectors.toMap(CheckUnit::getKey, unit -> sourceMap.get(unit.getKey())));
        Map<String, CheckEntry> targetDiffMap = Optional.ofNullable(unitDetailsMap.get(CheckStateEnum.TARGET_MORE)).orElse(Collections.emptyList()).stream()
                .collect(Collectors.toMap(CheckUnit::getKey, unit -> targetMap.get(unit.getKey())));
        CheckResult result = CheckResult.builder()
                .sourceDiffMap(sourceDiffMap)
                .targetDiffMap(targetDiffMap)
                .diffDetails(unitDetails)
                .diffDetailsMap(unitDetailsMap)
                .build();
        context.setCheckResult(result);
    }

    @Override
    public CheckCacheEnum checkCache() {
        return CheckCacheEnum.MEMORY;
    }
}

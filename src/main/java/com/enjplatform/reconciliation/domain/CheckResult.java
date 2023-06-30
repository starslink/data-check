package com.enjplatform.reconciliation.domain;

import com.enjplatform.reconciliation.enums.CheckStateEnum;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * 对账结果
 *
 * @author starslink
 */
@Data
@Builder
public class CheckResult {


    /**
     * 最开始的数据差异
     */
    private List<CheckUnit> preDetails;

    /**
     * 上游数据中不相同的数据
     */
    private Map<String, CheckEntry> sourceDiffMap;

    /**
     * 下游数据中不相同的数据
     */
    private Map<String, CheckEntry> targetDiffMap;

    /**
     * source 与 target 的详细差异
     */
    private List<CheckUnit> diffDetails;

    /**
     * 按类型 group by 后的详细差异
     */
    private Map<CheckStateEnum, List<CheckUnit>> diffDetailsMap;
}

package com.enjplatform.reconciliation.executor;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.map.MapUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.enjplatform.reconciliation.after.AfterCheckProcessor;
import com.enjplatform.reconciliation.after.DefaultAfterCheckProcessor;
import com.enjplatform.reconciliation.before.BeforeCheckProcessor;
import com.enjplatform.reconciliation.before.DefaultBeforeCheckProcessor;
import com.enjplatform.reconciliation.before.DefaultResourceReader;
import com.enjplatform.reconciliation.before.ResourceReader;
import com.enjplatform.reconciliation.check.CheckProcessor;
import com.enjplatform.reconciliation.check.CheckProcessorFactory;
import com.enjplatform.reconciliation.config.CheckConfig;
import com.enjplatform.reconciliation.domain.CheckContext;
import com.enjplatform.reconciliation.domain.CheckResult;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import com.enjplatform.reconciliation.enums.CheckStateEnum;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

/**
 * 对账执行器
 *
 * @author starslink
 */
@Builder
@Getter
public class CheckExecutor {

    private static final Log log = LogFactory.get();

    private final String id;

    private final BeforeCheckProcessor beforeCheckProcessor;

    private final CheckProcessor checkProcessor;

    private final AfterCheckProcessor afterCheckProcessor;

    private final CheckConfig checkConfig;

    private final ExecutorManager executorManager;

    /**
     * 创建执行器
     *
     * @param checkConfig 配置参数
     * @return 执行器
     */
    public static CheckExecutor buildExecutor(CheckConfig checkConfig) {
        //前置处理器组装
        ResourceReader resourceReader = Optional.ofNullable(checkConfig.getResourceReader())
                .orElse(new DefaultResourceReader(checkConfig.getSrcLoader(), checkConfig.getTargetLoader()));
        BeforeCheckProcessor beforeCheckProcessor = new DefaultBeforeCheckProcessor(resourceReader, checkConfig.getCheckPre());

        CheckCacheEnum checkCacheEnum = Optional.ofNullable(checkConfig.getCheckCacheEnum()).orElse(CheckCacheEnum.MEMORY);
        //对账处理器组装
        CheckProcessor checkProcessor = Optional.ofNullable(checkConfig.getCheckProcessor())
                .orElse(new CheckProcessorFactory().getCheckProcessor(checkCacheEnum));

        //后置处理器组装
        AfterCheckProcessor afterCheckProcessor = new DefaultAfterCheckProcessor(checkConfig.getCheckSync(), checkConfig.getCheckAfter());

        //对账管理器组装
        String id = Optional.ofNullable(checkConfig.getId()).orElse(checkConfig.getName());
        ExecutorManager executorManager = new DefaultExecutorManager(id);
        return CheckExecutor.builder()
                .id(id)
                .beforeCheckProcessor(beforeCheckProcessor)
                .checkProcessor(checkProcessor)
                .afterCheckProcessor(afterCheckProcessor)
                .checkConfig(checkConfig)
                .executorManager(executorManager)
                .build();
    }

    /**
     * 执行器业务逻辑
     */
    public CheckContext process(String date) {
        executorManager.initDate(date);
        CheckContext checkContext = CheckContext.builder()
                .id(this.id)
                .name(checkConfig.getName())
                .allowableErrorRange(checkConfig.getAllowableErrorRange())
                .date(date)
                .executorManager(executorManager)
                .build();
        log.info("对账开始,对账执行key:{}", executorManager.getExecutorKey());
        StopWatch stopWatch = new StopWatch(id);
        checkContext.setStopWatch(stopWatch);
        try {
            if (executorManager.isProcessing()) {
                //任务正在执行中
                log.info("当前任务正在执行,本次执行忽略,任务执行key:{}", executorManager.getExecutorKey());
                return checkContext;
            }
            stopWatch.start(beforeCheckProcessor.getClass().getName());
            Boolean doPre = beforeCheckProcessor.processBeforeCompare(checkContext);
            if (!doPre) {
                //任务正在执行中
                log.info("前置校验未通过,本次执行忽略,对账执行key:{}", executorManager.getExecutorKey());
                return checkContext;
            }
            stopWatch.stop();
            stopWatch.start(checkProcessor.getClass().getName());

            checkProcessor.compare(checkContext);

            CheckResult result = checkContext.getCheckResult();
            Map<CheckStateEnum, Integer> map = result.getDiffDetailsMap().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size()));
            log.info("对账执行key:{},对账结果:{}", executorManager.getExecutorKey(), MapUtil.of(Pair.of("sourceSize", checkContext.getSource().size()),
                    Pair.of("targetSize", checkContext.getTarget().size()),
                    Pair.of("different", result.getDiffDetails().size()),
                    Pair.of("details", map)));
            stopWatch.stop();
            stopWatch.start(afterCheckProcessor.getClass().getName());

            afterCheckProcessor.processAfterCompare(checkContext);
            stopWatch.stop();
        } catch (Exception e) {
            log.warn("对账异常结束", executorManager.getExecutorKey() + ":" + executorManager.getCurrentStatus(), e);
            log.error("对账异常结束", e);
            executorManager.pauseByError();
            throw e;
        }
        log.info(stopWatch.prettyPrint());
        log.info("对账结束:", executorManager.getExecutorKey());
        return checkContext;
    }
}

package com.enjplatform.reconciliation.config;

import com.enjplatform.reconciliation.after.AfterCheckProcessor;
import com.enjplatform.reconciliation.after.CheckAfter;
import com.enjplatform.reconciliation.after.CheckSync;
import com.enjplatform.reconciliation.before.BeforeCheckProcessor;
import com.enjplatform.reconciliation.before.CheckPre;
import com.enjplatform.reconciliation.before.ResourceLoader;
import com.enjplatform.reconciliation.before.ResourceReader;
import com.enjplatform.reconciliation.check.CheckProcessor;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * 对账配置
 *
 * @author starslink
 */
@Data
@Builder
public class CheckConfig {

    /**
     * 对账唯一标识:进度管理以id为主 name与id不可同时为空
     */
    private String id;

    /**
     * 对账名称: 名称可能重复
     */
    private String name;

    /**
     * 单批次对账数量(防止内存泄漏) 非必填
     */
    private Integer batchSize;

    /**
     * 是否多线程核对数据(默认单线程) 非必填
     */
    private Boolean isAsync;

    /**
     * 可允许误差范围 对比范围Math.abs(allowableErrorRange)
     */
    private BigDecimal allowableErrorRange;

    /**
     * 使用何种对账方式(默认内存) 非必填
     */
    private CheckCacheEnum checkCacheEnum;

    /**
     * redis配置
     */
    private RedisConfig redisConfig;

    //前置处理器配置-----------------------------------

    /**
     * 自定义前置处理器:可不依赖于ResourceReader
     */
    private BeforeCheckProcessor beforeCheckProcessor;

    /**
     * 前置校验: 可为空
     */
    private CheckPre checkPre;

    /**
     * 上游数据源
     */
    private ResourceLoader srcLoader;

    /**
     * 下游数据源
     */
    private ResourceLoader targetLoader;

    /**
     * 数据源读取 (包含上下游)
     */
    private ResourceReader resourceReader;

    //对账处理器配置-----------------------------------

    /**
     * 自定义对账处理器
     */
    private CheckProcessor checkProcessor;

    //后置处理器配置-----------------------------------

    /**
     * 自定义后置处理器: 可不依赖于CheckSync
     */
    private AfterCheckProcessor afterCheckProcessor;

    /**
     * 对账结果同步处理器: 可为空
     */
    private CheckSync checkSync;

    /**
     * 对账后置处理逻辑: 可为空
     */
    private CheckAfter checkAfter;
}

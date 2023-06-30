package com.enjplatform.reconciliation.domain;

import cn.hutool.core.date.StopWatch;
import com.enjplatform.reconciliation.executor.ExecutorManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import redis.clients.jedis.JedisPooled;

/**
 * 对账上下文
 *
 * @author starslink
 */
@Data
@Builder
public class CheckContext {

    /**
     * 对账id
     */
    private String id;

    /**
     * 对账名称
     */
    private String name;

    /**
     * 对账日期
     */
    private String date;

    /**
     * 可允许误差范围 对比范围Math.abs(allowableErrorRange)
     */
    private BigDecimal allowableErrorRange;

    /**
     * 对账源数据
     */
    private List<CheckEntry> source;

    /**
     * 对账目标数据
     */
    private List<CheckEntry> target;

    /**
     * 对账源数据
     */
    private Map<String, CheckEntry> sourceMap;

    /**
     * 对账目标数据
     */
    private Map<String, CheckEntry> targetMap;

    /**
     * 初始数据差异(可能存在相同key的情况，进行对账)
     */
    private List<CheckUnit> preDetails;

    /**
     * 对账结果
     */
    private CheckResult checkResult;

    /**
     * 周期管理
     */
    private ExecutorManager executorManager;

    /**
     * Jedis数据库连接池(全局唯一)
     */
    private JedisPooled jedisPooled;

    /**
     * 执行时间
     */
    private StopWatch stopWatch;
}

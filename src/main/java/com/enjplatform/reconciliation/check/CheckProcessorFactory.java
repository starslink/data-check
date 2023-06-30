package com.enjplatform.reconciliation.check;

import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CheckProcessorFactory
 * @Description 检查器工厂类
 * @Author starslink
 * @Date 2023/5/30 22:19
 * @Version 1.0
 */
public class CheckProcessorFactory {

    private static final Map<String, CheckProcessor> checkProcessor = new HashMap<>(16);

    public CheckProcessorFactory() {
        checkProcessor.put(CheckCacheEnum.MEMORY.getKey(), new MemoryCheckProcessor());
        checkProcessor.put(CheckCacheEnum.REDIS.getKey(), new RedisCheckProcessor());
    }

    /**
     * 获取检查器
     *
     * @param checkCacheEnum
     * @return
     */
    public CheckProcessor getCheckProcessor(CheckCacheEnum checkCacheEnum) {
        String checkCacheEnumKey = checkCacheEnum.getKey();
        if (!checkProcessor.containsKey(checkCacheEnumKey)) {
            return new MemoryCheckProcessor();
        }
        return checkProcessor.get(checkCacheEnumKey);
    }
}

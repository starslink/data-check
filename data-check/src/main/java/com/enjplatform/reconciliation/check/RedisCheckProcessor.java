package com.enjplatform.reconciliation.check;

import cn.hutool.core.util.ObjectUtil;
import com.enjplatform.reconciliation.domain.CheckContext;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import org.junit.Assert;
import redis.clients.jedis.JedisPooled;

/**
 * @ClassName RedisCheckProcessor
 * @Description redis比较器
 * @Author starslink
 * @Date 2023/5/30 22:12
 * @Version 1.0
 */
public class RedisCheckProcessor extends AbstractCheckProcessor {

    @Override
    public void doCompare(CheckContext context) {
        Assert.fail("暂不支持Redis比较器");
        JedisPooled jedisPooled = context.getJedisPooled();
        if (ObjectUtil.isNull(jedisPooled)) {
            Assert.fail("未查询到redis连接池相关信息");
        }
    }

    @Override
    public CheckCacheEnum checkCache() {
        return CheckCacheEnum.REDIS;
    }
}

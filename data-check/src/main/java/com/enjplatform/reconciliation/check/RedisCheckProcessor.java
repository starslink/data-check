package com.enjplatform.reconciliation.check;

import cn.hutool.core.util.ObjectUtil;
import com.enjplatform.reconciliation.domain.CheckContext;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import org.junit.Assert;
import redis.clients.jedis.JedisPooled;

/**
 * @ClassName RedisCheckProcessor
 * @Description redis�Ƚ���
 * @Author starslink
 * @Date 2023/5/30 22:12
 * @Version 1.0
 */
public class RedisCheckProcessor extends AbstractCheckProcessor {

    @Override
    public void doCompare(CheckContext context) {
        Assert.fail("�ݲ�֧��Redis�Ƚ���");
        JedisPooled jedisPooled = context.getJedisPooled();
        if (ObjectUtil.isNull(jedisPooled)) {
            Assert.fail("δ��ѯ��redis���ӳ������Ϣ");
        }
    }

    @Override
    public CheckCacheEnum checkCache() {
        return CheckCacheEnum.REDIS;
    }
}

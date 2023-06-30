package com.enjplatform.reconciliation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName CheckCacheEnum
 * @Description 对账缓存 默认内存 可选项Redis
 * @Author starslink
 * @Date 2023/5/30 21:58
 * @Version 1.0
 */
@Getter
public enum CheckCacheEnum {

    MEMORY("memory", "内存"),
    REDIS("redis", "分布式缓存");

    private String key;
    private String desc;

    CheckCacheEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}


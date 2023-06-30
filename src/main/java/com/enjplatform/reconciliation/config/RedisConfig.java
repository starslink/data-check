package com.enjplatform.reconciliation.config;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName RedisConfig
 * @Description redis配置文件
 * @Author starslink
 * @Date 2023/5/30 22:38
 * @Version 1.0
 */
@Data
@Builder
public class RedisConfig {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 地址
     */
    private String host;
    /**
     * 端口
     */
    private String port;

    /**
     * url
     */
    private String url;

    /**
     * key 格式化
     */
    private String keyFormat;

    /**
     * value格式化
     */
    private String valueFormat;
}

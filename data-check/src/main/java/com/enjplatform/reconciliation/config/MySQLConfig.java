package com.enjplatform.reconciliation.config;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName MySQLConfig
 * @Description mysql配置文件
 * @Author starslink
 * @Date 2023/5/30 22:39
 * @Version 1.0
 */
@Data
@Builder
public class MySQLConfig {

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
}

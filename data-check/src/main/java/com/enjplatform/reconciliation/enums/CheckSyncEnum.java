package com.enjplatform.reconciliation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author starslink
 */
@AllArgsConstructor
@Getter
public enum CheckSyncEnum {
    NO_SYNC(0, "未调账"),
    SYNC(1, "已调账"),
    //跨日等情况忽略
    IGNORE(2, "忽略");
    private Integer val;
    private String desc;
}

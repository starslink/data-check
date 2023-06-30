package com.enjplatform.reconciliation.enums;

import lombok.Getter;

/**
 * @ClassName CheckAdditionalEnum
 * @Description 对账附加使用源头数据还是目标数据附加
 * @Author starslink
 * @Date 2023/6/16 13:07
 * @Version 1.0
 */
@Getter
public enum CheckAdditionalEnum {

    NO_ADDITIONAL(0, "不需要附加数据"),
    SOURCE_ADDITIONAL(1, "源头数据附加"),
    TARGET_ADDITIONAL(2, "目标数据附加");

    private Integer val;
    private String desc;

    CheckAdditionalEnum(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }
}

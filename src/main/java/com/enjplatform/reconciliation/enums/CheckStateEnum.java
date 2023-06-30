package com.enjplatform.reconciliation.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author starslink
 * @desc 对账结果;1-单号匹配，金额不对  2-业务平台有单号，支付平台没有  3-支付平台有单号，业务平台没有 4-业务平台数据重复 5-支付平台数据重复
 */
@AllArgsConstructor
@Getter
public enum CheckStateEnum {
    DIFFERENT(1, "单号匹配，金额不对"),
    TARGET_MORE(2, "目标平台有单号，数据来源平台没有"),
    SOURCE_MORE(3, "数据来源平台有单号，目标平台没有"),
    TARGET_REPEAT(4, "目标数据重复"),
    SOURCE_REPEAT(5, "来源数据重复");

    private Integer val;
    private String desc;

    /**
     * 根据value值获取具体的枚举值信息
     *
     * @param val
     * @return
     */
    public static CheckStateEnum getCheckStateEnum(Integer val) {
        CheckStateEnum[] checkStateEnums = values();

        for (CheckStateEnum checkStateEnum : checkStateEnums) {
            if (checkStateEnum.getVal().equals(val)) {
                return checkStateEnum;
            }
        }
        return null;
    }

}

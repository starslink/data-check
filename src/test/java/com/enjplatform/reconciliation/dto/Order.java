package com.enjplatform.reconciliation.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.enjplatform.reconciliation.annotation.CheckField;
import com.enjplatform.reconciliation.annotation.CheckIdentity;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @ClassName Order
 * @Description 订单
 * @Author starslink
 * @Date 2023/5/30 17:49
 * @Version 1.0
 */
@Data
public class Order {

    /**
     * 订单编号
     */
    @ExcelProperty("交易号")
    @CheckIdentity
    private String orderCode;
    /**
     * 订单金额
     */
    @ExcelProperty("金额")
    @CheckField
    private BigDecimal price;
}

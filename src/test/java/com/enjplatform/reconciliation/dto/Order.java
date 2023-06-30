package com.enjplatform.reconciliation.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.enjplatform.reconciliation.annotation.CheckField;
import com.enjplatform.reconciliation.annotation.CheckIdentity;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @ClassName Order
 * @Description ����
 * @Author starslink
 * @Date 2023/5/30 17:49
 * @Version 1.0
 */
@Data
public class Order {

    /**
     * �������
     */
    @ExcelProperty("���׺�")
    @CheckIdentity
    private String orderCode;
    /**
     * �������
     */
    @ExcelProperty("���")
    @CheckField
    private BigDecimal price;
}

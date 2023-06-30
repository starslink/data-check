package com.enjplatform.reconciliation.before;

import com.enjplatform.reconciliation.domain.CheckContext;

/**
 * 前置处理器 准备对账的上下游数据
 *
 * @author  starslink
 */
public interface BeforeCheckProcessor {

    /**
     * 准备数据
     *
     * @param context
     * @return
     */
    Boolean processBeforeCompare(CheckContext context);

}

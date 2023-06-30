package com.enjplatform.reconciliation.after;

import com.enjplatform.reconciliation.domain.CheckContext;

/**
 * 对账后置处理器
 *
 * @author  starslink
 */
public interface AfterCheckProcessor {

    /**
     * 对账结束后需要处理的逻辑
     *
     * @param context 上下文
     */
    void processAfterCompare(CheckContext context);

}

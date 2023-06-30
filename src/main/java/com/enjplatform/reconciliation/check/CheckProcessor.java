package com.enjplatform.reconciliation.check;

import com.enjplatform.reconciliation.domain.CheckContext;

/**
 * 对账处理器
 *
 * @author  starslink
 */
public interface CheckProcessor {

    /**
     * 对账逻辑
     *
     * @param context 上下文
     */
    void compare(CheckContext context);
}

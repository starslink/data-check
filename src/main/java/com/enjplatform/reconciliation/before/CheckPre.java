package com.enjplatform.reconciliation.before;

import com.enjplatform.reconciliation.domain.CheckContext;

/**
 * @author starslink
 */
@FunctionalInterface
public interface CheckPre {

    /**
     * 前置处理逻辑
     *
     * @param context 上下文
     * @return
     */
    boolean doPre(CheckContext context);
}

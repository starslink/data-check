package com.enjplatform.reconciliation.after;

import com.enjplatform.reconciliation.domain.CheckContext;

/**
 * @author starslink
 */
@FunctionalInterface
public interface CheckAfter {

    /**
     * 后置处理逻辑
     *
     * @param context 上下文
     */
     void doAfter(CheckContext context);
}

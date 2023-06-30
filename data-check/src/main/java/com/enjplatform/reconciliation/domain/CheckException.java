package com.enjplatform.reconciliation.domain;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author starslink
 */
public class CheckException extends RuntimeException {

    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(Throwable cause) {
        super(ExceptionUtil.getMessage(cause), cause);
    }

    public CheckException(Throwable throwable, String messageTemplate, Object... params) {
        super(CharSequenceUtil.format(messageTemplate, params), throwable);
    }
}

package com.enjplatform.reconciliation.entry;

import cn.hutool.core.map.MapUtil;
import com.enjplatform.reconciliation.annotation.CheckField;
import com.enjplatform.reconciliation.annotation.CheckIdentity;
import com.enjplatform.reconciliation.domain.CheckAdapter;
import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author starslink
 */

@Data
@AllArgsConstructor
public class TestB implements CheckAdapter {

    @CheckIdentity
    String name;
    String test;

    @CheckField
    BigDecimal amount;

    @Override
    public String getKey() {
        return name + test;
    }

    @Override
    public Map<String, Object> getCheckData() {
        return MapUtil.of("amount", amount);
    }
}

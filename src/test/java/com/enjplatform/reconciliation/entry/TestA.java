package com.enjplatform.reconciliation.entry;

import cn.hutool.core.map.MapUtil;
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
public class TestA implements CheckAdapter {

    String name;

    String remark;

    BigDecimal amount;

    @Override
    public String getKey() {
        return name + remark;
    }

    @Override
    public Map<String, Object> getCheckData() {
        return MapUtil.of("amount", amount);
    }
}

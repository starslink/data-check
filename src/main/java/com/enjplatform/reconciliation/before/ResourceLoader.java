package com.enjplatform.reconciliation.before;

import com.enjplatform.reconciliation.domain.CheckEntry;
import java.util.List;

/**
 * @author starslink
 */
@FunctionalInterface
public interface ResourceLoader {

    List<CheckEntry> load(String date);
}
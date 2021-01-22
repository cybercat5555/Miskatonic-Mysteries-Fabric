package com.miskatonicmysteries.common.feature.interfaces;

import com.miskatonicmysteries.common.feature.Affiliation;

public interface Affiliated {
    Affiliation getAffiliation(boolean apparent);

    boolean isSupernatural();
}

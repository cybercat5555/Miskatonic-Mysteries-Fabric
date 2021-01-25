package com.miskatonicmysteries.common.feature.interfaces;

import com.miskatonicmysteries.common.feature.Affiliation;

import java.util.Optional;

public interface Affiliated {
    static Optional<Affiliated> of(Object context) {
        if (context instanceof Affiliated) {
            return Optional.of(((Affiliated) context));
        }
        return Optional.empty();
    }

    Affiliation getAffiliation(boolean apparent);

    boolean isSupernatural();
}

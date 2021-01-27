package com.miskatonicmysteries.common.feature.interfaces;

import java.util.Optional;

public interface Ascendant {
    static Optional<Ascendant> of(Object context) {
        if (context instanceof Ascendant) {
            return Optional.of(((Ascendant) context));
        }
        return Optional.empty();
    }

    int getStage();

    void setStage(int level);

    void syncMutationData();
}

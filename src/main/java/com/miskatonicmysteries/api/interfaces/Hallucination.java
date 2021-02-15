package com.miskatonicmysteries.api.interfaces;

import java.util.Optional;
import java.util.UUID;

public interface Hallucination {
    static Optional<Hallucination> of(Object context) {
        if (context instanceof Hallucination) {
            return Optional.of(((Hallucination) context));
        }
        return Optional.empty();
    }

    Optional<UUID> getHallucinationTarget();

    void setHallucinationTarget(Optional<UUID> target);
}

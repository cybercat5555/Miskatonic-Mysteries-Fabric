package com.miskatonicmysteries.common.feature.interfaces;

import java.util.Optional;

public interface Resonating {
    static Optional<Resonating> of(Object context) {
        if (context instanceof Resonating) {
            return Optional.of(((Resonating) context));
        }
        return Optional.empty();
    }

    float getResonance();

    void setResonance(float resonance);
}

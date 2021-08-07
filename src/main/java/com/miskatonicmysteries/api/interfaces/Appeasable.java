package com.miskatonicmysteries.api.interfaces;

import java.util.Optional;

public interface Appeasable {
    static Optional<Appeasable> of(Object context) {
        if (context instanceof Appeasable) {
            return Optional.of(((Appeasable) context));
        }
        return Optional.empty();
    }

    boolean isAppeased();

    int getAppeasedTicks();

    void setAppeasedTicks(int ticks);
}

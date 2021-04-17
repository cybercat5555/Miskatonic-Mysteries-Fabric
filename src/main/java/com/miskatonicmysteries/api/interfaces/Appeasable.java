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

    void setAppeasedTicks(int ticks);

    int getAppeasedTicks();

    int getHoldTicks();

    void setHoldTicks(int ticks);
}

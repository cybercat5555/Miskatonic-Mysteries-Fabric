package com.miskatonicmysteries.common.feature.interfaces;

import java.util.Map;
import java.util.Optional;

public interface Sanity {
    static Optional<Sanity> of(Object context) {
        if (context instanceof Sanity) {
            return Optional.of(((Sanity) context));
        }
        return Optional.empty();
    }

    int getSanity();

    void setSanity(int amount, boolean ignoreFactors);

    void setShocked(boolean shocked);

    boolean isShocked();

    void addSanityCapExpansion(String name, int amount);

    void removeSanityCapExpansion(String name);

    Map<String, Integer> getSanityCapExpansions();
    void syncSanityData();
    int getMaxSanity();
}

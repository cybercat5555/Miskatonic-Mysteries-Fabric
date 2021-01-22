package com.miskatonicmysteries.common.feature.interfaces;

import java.util.Map;

public interface Sanity {
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

package com.miskatonicmysteries.common.feature.sanity;

import java.util.Map;

public interface ISanity {
    int getSanity();
    void setSanity(int amount);

    void setShocked(boolean shocked);
    boolean isShocked();

    void addSanityCapExpansion(String name, int amount);
    void removeSanityCapExpansion(String name);
    Map<String, Integer> getSanityCapExpansions();
    void syncSanityData();
    int getMaxSanity();

}

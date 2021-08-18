package com.miskatonicmysteries.api.interfaces;

import com.miskatonicmysteries.api.registry.Blessing;

import java.util.List;
import java.util.Optional;

public interface Ascendant {
    static Optional<Ascendant> of(Object context) {
        if (context instanceof Ascendant) {
            return Optional.of(((Ascendant) context));
        }
        return Optional.empty();
    }

    void addBlessing(Blessing blessing);

    boolean removeBlessing(Blessing blessing);

    List<Blessing> getBlessings();

    int getAscensionStage();

    void setAscensionStage(int level);
}

package com.miskatonicmysteries.common.feature.interfaces;

import com.miskatonicmysteries.common.feature.blessing.Blessing;

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

    void syncBlessingData(); //technically redundant with expansion packets existing ?
}

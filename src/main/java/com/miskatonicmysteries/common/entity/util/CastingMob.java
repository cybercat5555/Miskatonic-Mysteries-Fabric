package com.miskatonicmysteries.common.entity.util;

import com.miskatonicmysteries.common.feature.spell.Spell;

public interface CastingMob {
    int getCastTime();

    void setCastTime(int castTime);

    boolean isCasting();

    Spell getCurrentSpell();

    void setCurrentSpell(Spell spell);

    Spell selectSpell();
}

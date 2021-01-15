package com.miskatonicmysteries.common.feature.spell;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SpellCaster {
    int getPowerPool();

    void setPowerPool(int amount);

    int getMaxSpells();

    void setMaxSpells(int amount);

    List<Spell> getSpells();

    Set<SpellEffect> getLearnedEffects();

    void learnEffect(SpellEffect effect);

    Map<SpellMedium, Integer> getAvailableMediums();

    void setMediumAvailability(SpellMedium medium, int count);

    void syncSpellData();
}

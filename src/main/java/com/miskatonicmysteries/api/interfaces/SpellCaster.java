package com.miskatonicmysteries.api.interfaces;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.spell.Spell;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SpellCaster {
    static Optional<SpellCaster> of(Object context) {
        if (context instanceof SpellCaster) {
            return Optional.of(((SpellCaster) context));
        }
        return Optional.empty();
    }

    int getPowerPool();

    void setPowerPool(int amount);

    int getMaxSpells();

    void setMaxSpells(int amount);

    List<Spell> getSpells();

    Set<SpellEffect> getLearnedEffects();

    void learnEffect(SpellEffect effect);

    Set<SpellMedium> getLearnedMediums();

    void learnMedium(SpellMedium medium);

    int getSpellCooldown();

    void setSpellCooldown(int ticks);

    void syncSpellData();
}

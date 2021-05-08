package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.effect.*;
import net.minecraft.util.registry.Registry;

public class MMSpellEffects {
    public static final SpellEffect DAMAGE = new DamageSpellEffect();
    public static final SpellEffect HEAL = new HealSpellEffect();
    public static final SpellEffect RESISTANCE = new ResistanceSpellEffect();
    public static final SpellEffect KNOCKBACK = new KnockBackSpellEffect();
    public static final SpellEffect MANIA = new ManiaSpellEffect();
    public static final SpellEffect TENTACLES = new TentacleSpellEffect();
    public static final SpellEffect IGNITE = new FireSpellEffect();
    public static final SpellEffect HARROWS = new HarrowSpellEffect();


    public static void init() {
        register(DAMAGE);
        register(HEAL);
        register(RESISTANCE);
        register(KNOCKBACK);
        register(MANIA);
        register(TENTACLES);
        register(IGNITE);
        register(HARROWS);
    }

    private static void register(SpellEffect effect) {
        Registry.register(MMRegistries.SPELL_EFFECTS, effect.getId(), effect);
    }
}

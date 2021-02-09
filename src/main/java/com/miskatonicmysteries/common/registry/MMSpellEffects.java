package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.common.feature.spell.effect.DamageSpellEffect;
import com.miskatonicmysteries.common.feature.spell.effect.HealSpellEffect;
import com.miskatonicmysteries.common.feature.spell.effect.KnockBackSpellEffect;
import com.miskatonicmysteries.common.feature.spell.effect.ResistanceSpellEffect;
import net.minecraft.util.registry.Registry;

public class MMSpellEffects {
    public static final SpellEffect DAMAGE = new DamageSpellEffect();
    public static final SpellEffect HEAL = new HealSpellEffect();
    public static final SpellEffect RESISTANCE = new ResistanceSpellEffect();
    public static final SpellEffect KNOCKBACK = new KnockBackSpellEffect();

    public static void init() {
        register(DAMAGE);
        register(HEAL);
        register(RESISTANCE);
        register(KNOCKBACK);
    }

    private static void register(SpellEffect effect) {
        Registry.register(MMRegistries.SPELL_EFFECTS, effect.getId(), effect);
    }
}

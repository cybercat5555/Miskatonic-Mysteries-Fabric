package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SelfSpellMedium extends SpellMedium {
    public SelfSpellMedium() {
        super(new Identifier(Constants.MOD_ID, "self"));
    }

    @Override
    public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity, boolean backfires) {
        return !caster.isDead() && effect.effect(world, caster, caster, caster.getPos(), this, intensity, caster, backfires);
    }

    @Override
    public float getBurnoutRate(LivingEntity caster) {
        return 0.1F;
    }
}

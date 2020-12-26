package com.miskatonicmysteries.common.feature.spell;

import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SpellEffectResistance extends SpellEffect {
    public SpellEffectResistance() {
        super(new Identifier(Constants.MOD_ID, "resistance"), null, 0x81808C);
    }

    @Override
    public boolean effect(World world, LivingEntity caster, @Nullable Entity target, SpellMedium medium, int intensity) {
        if (world.isClient && target != null) {
            spawnParticleEffectsOnTarget(this, target);
        }
        if (!(target instanceof LivingEntity)) return false;
        ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1200 + 200 * intensity, Math.min(intensity, 2), true, true));
        return true;
    }
}

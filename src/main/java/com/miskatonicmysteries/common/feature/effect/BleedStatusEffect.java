package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.lib.ModParticles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class BleedStatusEffect extends StatusEffect {
    public BleedStatusEffect() {
        super(StatusEffectType.HARMFUL, 0xFF0000);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.world.random.nextFloat() < 0.1 + (0.1 * amplifier)) {
            entity.world.addParticle(ModParticles.DRIPPING_BLOOD, entity.getX() + entity.getRandom().nextGaussian() * 0.5F * entity.getWidth(), entity.getY() + entity.getRandom().nextGaussian() * 0.5F * entity.getHeight(), entity.getZ() + entity.getRandom().nextGaussian() * 0.5F * entity.getWidth(), 0, 0, 0);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

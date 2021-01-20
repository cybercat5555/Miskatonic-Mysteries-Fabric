package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.handler.networking.packet.s2c.BloodParticlePacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class BleedStatusEffect extends StatusEffect {
    public BleedStatusEffect() {
        super(StatusEffectType.HARMFUL, 0xFF0000);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (!entity.world.isClient && entity.world.random.nextFloat() < 0.1 + (0.1 * amplifier)) {
            BloodParticlePacket.send(entity);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

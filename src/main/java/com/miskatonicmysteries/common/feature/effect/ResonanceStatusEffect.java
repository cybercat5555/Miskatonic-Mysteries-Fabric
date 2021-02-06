package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.feature.interfaces.Resonating;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class ResonanceStatusEffect extends StatusEffect {
    public ResonanceStatusEffect() {
        super(StatusEffectType.NEUTRAL, 0xBE00FF);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        float targetIntensity = amplifier * 0.35F;
        Resonating.of(entity).ifPresent(resonating -> {
            if (resonating.getResonance() < targetIntensity) {
                resonating.setResonance(resonating.getResonance() + targetIntensity / 100F + 0.01F);
            }
        });
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}

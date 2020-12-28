package com.miskatonicmysteries.common.feature.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;

public class OvermedicalizedStatusEffect extends StatusEffect {
    public OvermedicalizedStatusEffect() {
        super(StatusEffectType.NEUTRAL, 0x000000);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if (amplifier >= 0) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 3600, amplifier));
            if (amplifier >= 1) entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 3600, 1));
            if (amplifier >= 2) entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1200, amplifier));
        }
    }
}

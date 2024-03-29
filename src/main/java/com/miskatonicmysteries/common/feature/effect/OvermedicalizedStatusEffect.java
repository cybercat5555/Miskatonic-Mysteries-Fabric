package com.miskatonicmysteries.common.feature.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class OvermedicalizedStatusEffect extends StatusEffect {

	public OvermedicalizedStatusEffect() {
		super(StatusEffectCategory.NEUTRAL, 0x000000);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (amplifier >= 0) {
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, amplifier));
			if (amplifier >= 1) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 1));
			}
			if (amplifier >= 2) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1200, amplifier));
			}
		}
	}
}

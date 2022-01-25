package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.registry.MMStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ClairvoyanceStatusEffect extends StatusEffect {

	public ClairvoyanceStatusEffect() {
		super(StatusEffectCategory.NEUTRAL, 0xFFFFFF);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);

		if (entity.age % 20 == 0) {
			MMStatusEffects.intoxicatedUpdate(entity, amplifier);
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}

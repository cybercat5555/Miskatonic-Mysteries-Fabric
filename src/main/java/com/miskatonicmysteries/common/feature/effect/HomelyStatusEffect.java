package com.miskatonicmysteries.common.feature.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class HomelyStatusEffect extends StatusEffect {

	public HomelyStatusEffect() {
		super(StatusEffectCategory.BENEFICIAL, 0xFFFFEE);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient && entity.age % 20 == 0) {
			entity.heal(amplifier + 1);
			if (entity instanceof PlayerEntity p) {
				p.getHungerManager().add(1, 1.5F);
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}

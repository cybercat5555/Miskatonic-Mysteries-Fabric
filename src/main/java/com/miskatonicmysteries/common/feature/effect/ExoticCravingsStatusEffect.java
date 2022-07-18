package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;

public class ExoticCravingsStatusEffect extends StatusEffect {

	public ExoticCravingsStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0xAA0000);
	}

	public static void onFoodEaten(LivingEntity entity, ItemStack stack) {
		if (entity.hasStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS)) {
			if (!stack.isIn(Constants.Tags.GROSS_FOOD)) {
				entity.damage(DamageSource.STARVE, 4);
			} else {
				StatusEffectInstance instance = entity.getStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS);
				entity.removeStatusEffect(MMStatusEffects.EXOTIC_CRAVINGS);
				entity.addStatusEffect(new StatusEffectInstance(instance.getEffectType(), instance
					.getDuration() - (200 - instance.getAmplifier() * 40), instance.getAmplifier(), false, true));
			}
		}
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.age % 20 == 0) {
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 200, 2, true, false, false));
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}

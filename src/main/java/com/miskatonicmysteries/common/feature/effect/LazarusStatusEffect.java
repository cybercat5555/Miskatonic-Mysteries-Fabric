package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.registry.MMStatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class LazarusStatusEffect extends StatusEffect {

	public LazarusStatusEffect() {
		super(StatusEffectCategory.NEUTRAL, 0xEA9800);
	}

	public static boolean revive(PlayerEntity player) {
		int count =
			player.hasStatusEffect(MMStatusEffects.LAZARUS) ? player.getStatusEffect(MMStatusEffects.LAZARUS).getAmplifier() + 1 : 0;
		player.addStatusEffect(new StatusEffectInstance(MMStatusEffects.LAZARUS, 20000, count, true, false, true));
		if (count < 4) {
			player.setHealth(player.getMaxHealth() / (count + 1));
		}
		return count < 4;
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (amplifier >= 0) {
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 1200, amplifier));
			if (amplifier >= 1) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 0));
			}
			if (amplifier >= 2) {
				entity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 1200, 0));
			}
		}
	}
}

package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.handler.networking.packet.s2c.BloodParticlePacket;
import com.miskatonicmysteries.common.registry.MMStatusEffects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class BleedStatusEffect extends StatusEffect {

	public BleedStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0xFF0000);
	}

	public static void onTryHeal(LivingEntity entity, CallbackInfo info) {
		if (entity.hasStatusEffect(MMStatusEffects.BLEED) && entity.getRandom().nextFloat() < 0.4 + 0.2 *
			entity.getStatusEffect(MMStatusEffects.BLEED).getAmplifier()) {
			info.cancel();
		}
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

package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public class TranquilizedStatusEffect extends StatusEffect {

	public TranquilizedStatusEffect() {
		super(StatusEffectType.BENEFICIAL, 0x2E219E);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof MobEntity && ((MobEntity) entity).getTarget() == null && entity.age % 60 == 0) {
			if (entity.getRandom().nextFloat() < (0.1 * amplifier)) {
				onApplied(entity, entity.getAttributes(), amplifier);
			}
		}
		if (entity instanceof PlayerEntity && ((PlayerEntity) entity).isSleepingLongEnough()) {
			if (isLethal(entity, amplifier)) {
				entity.damage(Constants.DamageSources.SLEEP, 4000);
			} else {
				Sanity.of(entity).ifPresent(sanity -> {
					sanity.setSanity((int) (sanity.getSanity() + MiskatonicMysteries.config.sanity.tranquilizedSanityBonus * Math
						.min((amplifier + 2) / 2F, 3F)), true);
					if (entity.getRandom().nextFloat() < MiskatonicMysteries.config.sanity.tranquilizedSanityCapRegainChance) {
						for (String s : sanity.getSanityCapExpansions().keySet()) {
							if (sanity.getSanityCapExpansions().get(s) < 0) {
								int value = sanity.getSanityCapExpansions().get(s) + (amplifier * 5);
								sanity.removeSanityCapExpansion(s);
								if (value <= 0) {
									sanity.addSanityCapExpansion(s, value);
								}
							}
						}
					}
				});
				entity.damage(Constants.DamageSources.SLEEP, 2);

			}
			entity.removeStatusEffect(this);
		}
		entity.removeStatusEffect(MMStatusEffects.MANIA);

		if (entity.age % 20 == 0) {
			MMStatusEffects.intoxicatedUpdate(entity, amplifier);
		}
	}

	private boolean isLethal(LivingEntity entity, int amplifier) {
		return entity.getHealth() < 5 + Math.min(amplifier * 2, 5);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof MobEntity) {
			MobEntity mob = (MobEntity) entity;
			mob.setTarget(null);
			mob.setAttacking(false);
		}
	}
}

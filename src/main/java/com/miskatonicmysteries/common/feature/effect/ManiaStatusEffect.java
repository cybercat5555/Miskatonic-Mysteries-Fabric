package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

public class ManiaStatusEffect extends StatusEffect {

	public ManiaStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0xA42100);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof MobEntity && ((MobEntity) entity).getTarget() == null && entity.age % 60 == 0) {
			if (entity.getRandom().nextFloat() < (0.1 * amplifier)) {
				onApplied(entity, entity.getAttributes(), amplifier);
			}
		}
		Sanity.of(entity).ifPresent(sanity -> {
			insanityDeath(entity, (Sanity) entity, amplifier);
			if (entity.age % 120 == 20 && entity.getRandom().nextFloat() < 0.05 * (amplifier + 1)) {
				InsanityHandler.handleInsanityEvents((PlayerEntity) entity);
			}
		});

		if (entity.age % 20 == 0) {
			MMStatusEffects.intoxicatedUpdate(entity, amplifier);
		}
	}

	private void insanityDeath(LivingEntity entity, Sanity sanity, int amplifier) {
		if (sanity.getSanity() < MiskatonicMysteries.config.sanity.deadlyInsanityThreshold
			&& entity.age % Math.min(60 - amplifier * 3, 20) == 0 && entity.getRandom().nextFloat() > (sanity.getSanity()
			/ (float) MiskatonicMysteries.config.sanity.deadlyInsanityThreshold)) {
			entity.damage(Constants.DamageSources.INSANITY, 666);
			sanity.setSanity(MiskatonicMysteries.config.sanity.deadlyInsanityThreshold + 50, true);
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof MobEntity) {
			entity.world.getOtherEntities(entity, entity.getBoundingBox().expand(8, 3, 8),
				target -> target instanceof LivingEntity && EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target))
				.stream().findAny().ifPresent(value -> ((MobEntity) entity).setTarget((LivingEntity) value));
		}
	}
}

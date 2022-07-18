package com.miskatonicmysteries.common.feature.effect;

import com.miskatonicmysteries.common.registry.MMStatusEffects;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;


public class BrainDrainStatusEffect extends StatusEffect {

	public BrainDrainStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0x6B5D28);
	}

	public static int onExperienceGained(PlayerEntity player, int experience) {
		if (player.hasStatusEffect(MMStatusEffects.BRAIN_DRAIN) && experience > 0) {
			return Math.round(experience * 0.75F);
		}
		return experience;
	}

	public static float onDamage(LivingEntity entity, DamageSource source, float original) {
		if (source != null && source.isMagic() && source.getAttacker() instanceof LivingEntity l && l.hasStatusEffect(MMStatusEffects.BRAIN_DRAIN)) {
			return original - (3 + l.getStatusEffect(MMStatusEffects.BRAIN_DRAIN).getAmplifier());
		}
		return original;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		super.applyUpdateEffect(entity, amplifier);
		if (!entity.world.isClient && entity instanceof PlayerEntity p && entity.getRandom().nextInt(100) <= amplifier) {
			int amount = MathHelper.clamp(entity.getRandom().nextInt(20) + 5, 5, p.totalExperience);
			p.addExperience(-amount);
			ExperienceOrbEntity orb = new ExperienceOrbEntity(p.world, p.getParticleX(1), p.getRandomBodyY(), p.getParticleZ(1), amount);
			orb.setVelocity(orb.getVelocity().multiply(3));
			p.experiencePickUpDelay = 4;
			p.world.spawnEntity(orb);
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}

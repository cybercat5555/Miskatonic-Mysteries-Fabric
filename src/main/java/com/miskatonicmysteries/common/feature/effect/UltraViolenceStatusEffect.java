package com.miskatonicmysteries.common.feature.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

public class UltraViolenceStatusEffect extends StatusEffect {

	public UltraViolenceStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0xFFBB00);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (!entity.world.isClient && entity.getRandom().nextFloat() < Math.min(0.02 * (1 + amplifier), 0.1F)) {
			Vec3d vec3d = entity.getCameraPosVec(1);
			Vec3d vec3d2 = entity.getRotationVec(1);
			Vec3d vec3d3 = vec3d.add(vec3d2.x * 4, vec3d2.y * 4, vec3d2.z * 4);
			double distance = Math.pow(4, 2);
			EntityHitResult hit = ProjectileUtil.getEntityCollision(entity.world, entity, vec3d, vec3d3,
																	entity.getBoundingBox().stretch(vec3d2.multiply(distance))
																		.expand(1.0D, 1.0D, 1.0D),
																	(target) -> !target.isSpectator() && target.isAttackable());
			if (hit != null && hit.getEntity() != null) {
				if (entity instanceof PlayerEntity) {
					((PlayerEntity) entity).attack(hit.getEntity());
					((PlayerEntity) entity).resetLastAttackedTicks();
				} else {
					entity.tryAttack(hit.getEntity());
				}
				entity.swingHand(Hand.MAIN_HAND, entity instanceof PlayerEntity);
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}
}

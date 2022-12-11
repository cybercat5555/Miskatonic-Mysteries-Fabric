package com.miskatonicmysteries.common.feature.spell.medium;

import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.BoltEntity;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.entity.LivingEntityAccessor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BoltSpellMedium extends SpellMedium {

	public BoltSpellMedium() {
		super(new Identifier(Constants.MOD_ID, "bolt"));
	}

	@Override
	public boolean cast(World world, LivingEntity caster, SpellEffect effect, int intensity) {
		double distance = Math.pow(getMaxDistance(), 2);
		Vec3d vec3d = caster.getCameraPosVec(1);
		Vec3d vec3d2 = caster.getRotationVec(1);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * getMaxDistance(), vec3d2.y * getMaxDistance(), vec3d2.z * getMaxDistance());
		HitResult blockHit = world
			.raycast(new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, caster));
		EntityHitResult hit = ProjectileUtil.getEntityCollision(world, caster, vec3d, vec3d3,
				caster.getBoundingBox().stretch(vec3d2.multiply(distance)).expand(1.0D, 1.0D, 1.0D),
				(target) -> !target.isSpectator()  && caster.isAttackable() && caster.canSee(target));

		if (!world.isClient && blockHit.getPos() != null) {
			BoltEntity bolt = new BoltEntity(caster,
											 hit != null && hit.getEntity() != null ? hit.getEntity().distanceTo(caster)
																					: blockHit.getPos().distanceTo(caster.getPos()),
											 effect.getColor(caster));
			world.spawnEntity(bolt);
		}
		if (hit != null && blockHit.squaredDistanceTo(caster) > hit.getEntity().squaredDistanceTo(caster)) {
			Entity hitEntity = hit.getEntity();
			if (hitEntity instanceof LivingEntity l && l.blockedByShield(DamageSource.mob(caster))) {
				((LivingEntityAccessor) l).callDamageShield(intensity);
				world.sendEntityStatus(l, (byte) 29);
				return false;
			}
			boolean hadEffect = effect.effect(world, caster, hitEntity, hit.getPos(), this, intensity, caster);
			if (intensity > 0) {
				jumpBolt(world, caster, hit.getPos().add(0, hitEntity.getHeight() / 2.0, 0), hitEntity, new ArrayList<>(), effect,
						 MathHelper.clamp(intensity, 0, 4));
			}
			return hadEffect;
		} else {
			return effect.effect(world, caster, null, blockHit.getPos(), this, intensity, caster);
		}
	}

	@Override
	public float getCooldownModifier(LivingEntity caster) {
		return 1.5F;
	}

	private void jumpBolt(World world, LivingEntity caster, Vec3d startPos, Entity specificTarget, List<Entity> targets, SpellEffect effect,
						  int recursions) {
		List<Entity> otherTargets = getOtherTargets(world, specificTarget, targets);
		if (targets.isEmpty()) {
			targets.add(specificTarget);
		}
		targets.addAll(otherTargets);
		if (recursions > 0) {
			recursions--;
		}
		for (Entity entity : List.copyOf(otherTargets)) {
			Vec3d to = entity.getPos().add(0, entity.getHeight() / 2F, 0);
			if (!world.isClient) {
				BoltEntity bolt = new BoltEntity(world, startPos, to.subtract(startPos), effect.getColor(caster));
				world.spawnEntity(bolt);
			}
			effect.effect(world, caster, entity, to, this, Math.max(recursions - 1, 0), caster);
			if (recursions > 0) {
				jumpBolt(world, caster, to, entity, otherTargets, effect, recursions);
			}
		}
	}

	private List<Entity> getOtherTargets(World world, Entity target, List<Entity> totalTargets) {
		Box box = target.getBoundingBox().expand(2, 2, 2);
		boolean aliveTargets = target instanceof LivingEntity;
		List<Entity> targets = world.getOtherEntities(target, box,
													  entity -> (!aliveTargets || entity instanceof MobEntity) && !totalTargets.contains(entity))
			.stream().sorted(Comparator.comparingDouble(e -> e.distanceTo(target))).collect(Collectors.toList());
		return targets.subList(0, Math.min(2, targets.size()));
	}

	private int getMaxDistance() {
		return 24;
	}
}

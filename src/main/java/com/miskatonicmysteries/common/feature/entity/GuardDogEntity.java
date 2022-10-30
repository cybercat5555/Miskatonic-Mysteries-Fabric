package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.common.feature.entity.painting.ManosPaintingEntity;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SmokeEffectPacket;
import com.miskatonicmysteries.common.registry.MMEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.EnumSet;

public class GuardDogEntity extends PathAwareEntity {
	protected int originPaintingId;

	public GuardDogEntity(EntityType<? extends GuardDogEntity> entityType, World world) {
		super(entityType, world);
	}

	public GuardDogEntity(ManosPaintingEntity painting) {
		super(MMEntities.GUARD_DOG, painting.world);
		this.originPaintingId = painting.getId();
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new PounceAtTargetGoal(this, 1.2F));
		this.goalSelector.add(0, new AttackGoal(this) {
			@Override
			public boolean shouldContinue() {
				if (super.shouldContinue()) {
					ManosPaintingEntity painting = getPainting();
					if (painting != null) {
						return painting.distanceTo(GuardDogEntity.this) < 16;
					}
				}
				return false;
			}

			@Override
			public void stop() {
				super.stop();
				setTarget(null);
			}
		});
		this.goalSelector.add(1, new ReturnToPaintingGoal());
		this.targetSelector.add(0, new ActiveTargetGoal<>(this, MobEntity.class, true, living -> {
			ManosPaintingEntity painting = getPainting();
			if (painting != null) {
				return painting.distanceTo(living) < 16 && painting.canTarget(living);
			}
			return false;
		}));
		this.targetSelector.add(1, new RevengeGoal(this));
	}
	@Override
	public void remove(RemovalReason reason) {
		if(!this.world.isClient()){
			SmokeEffectPacket.send(this);
		}
		ManosPaintingEntity painting = getPainting();
		if (painting != null) {
			if (reason == RemovalReason.KILLED) {
				painting.startResting();
				painting.setPaintingStatus((byte) 2);
			}else {
				painting.setPaintingStatus((byte) 0);
			}
		}
		super.remove(reason);
	}

	@Override
	protected void updatePostDeath() {
		if (!this.world.isClient()) {
			this.remove(Entity.RemovalReason.KILLED);;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && age % 20 == 0) {
			ManosPaintingEntity painting = getPainting();
			if (age > 1200 || painting == null || painting.distanceTo(this) > 20) {
				remove(RemovalReason.DISCARDED);
			}
		}
	}

	private ManosPaintingEntity getPainting() {
		Entity entity = world.getEntityById(originPaintingId);
		if (entity instanceof ManosPaintingEntity m) {
			return m;
		}
		return null;
	}

	private class ReturnToPaintingGoal extends Goal {
		ManosPaintingEntity painting;
		int ticks;
		public ReturnToPaintingGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		}

		@Override
		public boolean canStart() {
			if (getPainting() != null) {
				return getTarget() == null || getPainting().distanceTo(GuardDogEntity.this) > 16;
			}
			return false;
		}

		@Override
		public void start() {
			super.start();
			painting = getPainting();
			getNavigation().startMovingTo(painting, 1);
			ticks = 0;
		}

		@Override
		public void tick() {
			super.tick();
			if (painting != null) {
				if (distanceTo(painting) < 2 || ticks++ > 100) {
					painting.setPaintingStatus((byte) 0);
					stop();
					remove(RemovalReason.DISCARDED);
				}
			}
			if (!getNavigation().isFollowingPath()) {
				stop();
			}
		}

		@Override
		public void stop() {
			super.stop();
			getNavigation().stop();
		}

		@Override
		public boolean shouldContinue() {
			return hurtTime == 0 && getTarget() == null;
		}
	}
}

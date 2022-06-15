package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.EnumSet;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

public class HarrowEntity extends PathAwareEntity { //mostly copies Vex code

	protected static final TrackedData<Byte> HARROW_FLAGS = DataTracker.registerData(HarrowEntity.class, TrackedDataHandlerRegistry.BYTE);
	protected static final TrackedData<Integer> LIFETICKS = DataTracker
		.registerData(HarrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public boolean summoned = true;
	private LivingEntity owner;

	public HarrowEntity(EntityType<? extends HarrowEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new HarrowMoveControl(this);
		this.experiencePoints = 3;
	}

	public static DefaultAttributeContainer.Builder createHarrowAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE);
	}

	public static boolean canSpawn(EntityType<HarrowEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if (pos.getY() > world.getSeaLevel()) {
			return world.isAir(pos);
		}
		return false;
	}

	@Override
	public void move(MovementType type, Vec3d movement) {
		super.move(type, movement);
		this.checkBlockCollision();
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new ChargeTargetGoal());
		this.goalSelector.add(2, new LookAtTargetGoal());
		this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.add(4, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.add(1, new TrackOwnerTargetGoal(this));
		this.targetSelector.add(2, new RevengeGoal(this));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, false, false, (living) -> getOwner() == null));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(HARROW_FLAGS, (byte) 0);
		this.dataTracker.startTracking(LIFETICKS, 100);
	}

	@Override
	protected int getXpToDrop(PlayerEntity player) {
		return owner instanceof PlayerEntity ? 0 : experiencePoints;
	}

	@Override
	public void tick() {
		this.noClip = true;
		super.tick();
		this.noClip = false;
		this.setNoGravity(true);
		if (!world.isClient && summoned) {
			if (getLifeTicks() > 0) {
				setLifeTicks(getLifeTicks() - 1);
			} else {
				remove(RemovalReason.KILLED);
			}
		}
		if (world.isClient && age % 5 == 0) {
			world.addParticle(MMParticles.AMBIENT_MAGIC, getParticleX(1F), getRandomBodyY(), getParticleZ(1F), -getVelocity().x,
							  -getVelocity().y, -getVelocity().z);
		}
	}

	public int getLifeTicks() {
		return dataTracker.get(LIFETICKS);
	}

	public void setLifeTicks(int lifeTicks) {
		this.dataTracker.set(LIFETICKS, lifeTicks);
	}

	protected SoundEvent getAmbientSound() {
		return MMSounds.ENTITY_HARROW_AMBIENT;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putInt("LifeTicks", getLifeTicks());
		tag.putBoolean(Constants.NBT.SUMMONED, summoned);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		setLifeTicks(tag.getInt("LifeTicks"));
		summoned = tag.getBoolean(NBT.MONSTER);
	}

	@Nullable
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
								 @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		if (spawnReason != SpawnReason.MOB_SUMMONED) {
			summoned = false;
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}

	public LivingEntity getOwner() {
		return this.owner;
	}

	public void setOwner(LivingEntity owner) {
		this.owner = owner;
	}

	public boolean isCharging() {
		return this.areFlagsSet(1);
	}

	private boolean areFlagsSet(int mask) {
		int i = this.dataTracker.get(HARROW_FLAGS);
		return (i & mask) != 0;
	}

	public void setCharging(boolean charging) {
		this.setHarrowFlag(1, charging);
	}

	private void setHarrowFlag(int mask, boolean value) {
		int i = this.dataTracker.get(HARROW_FLAGS);
		if (value) {
			i |= mask;
		} else {
			i &= ~mask;
		}

		this.dataTracker.set(HARROW_FLAGS, (byte) (i & 255));
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return MMSounds.ENTITY_HARROW_HURT;
	}

	protected SoundEvent getDeathSound() {
		return MMSounds.ENTITY_HARROW_DEATH;
	}

	class TrackOwnerTargetGoal extends TrackTargetGoal {

		private final TargetPredicate TRACK_OWNER_PREDICATE = TargetPredicate.createNonAttackable().ignoreVisibility()
			.ignoreDistanceScalingFactor();

		public TrackOwnerTargetGoal(PathAwareEntity mob) {
			super(mob, false);
		}

		@Override
		public boolean canStart() {
			return owner != null && owner.getAttacking() != null && this.canTrack(owner.getAttacking(), this.TRACK_OWNER_PREDICATE);
		}

		@Override
		public void start() {
			setTarget(owner.getAttacking());
			super.start();
		}
	}

	class LookAtTargetGoal extends Goal {

		public LookAtTargetGoal() {
			this.setControls(EnumSet.of(Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return !getMoveControl().isMoving() && random.nextInt(7) == 0;
		}

		@Override
		public boolean shouldContinue() {
			return false;
		}

		@Override
		public void tick() {
			BlockPos blockPos = getBlockPos();
			for (int i = 0; i < 3; ++i) {
				BlockPos blockPos2 = blockPos.add(random.nextInt(15) - 7, random.nextInt(11) - 5, random.nextInt(15) - 7);
				if (world.isAir(blockPos2)) {
					moveControl.moveTo((double) blockPos2.getX() + 0.5D, (double) blockPos2.getY() + 0.5D, (double) blockPos2.getZ() + 0.5D,
									   0.25D);
					if (getTarget() == null) {
						getLookControl()
							.lookAt((double) blockPos2.getX() + 0.5D, (double) blockPos2.getY() + 0.5D, (double) blockPos2.getZ() + 0.5D,
									180.0F, 20.0F);
					}
					break;
				}
			}

		}
	}

	class ChargeTargetGoal extends Goal {

		public ChargeTargetGoal() {
			this.setControls(EnumSet.of(Control.MOVE));
		}

		@Override
		public boolean canStart() {
			if (getTarget() != null && !getMoveControl().isMoving() && random.nextInt(7) == 0) {
				return squaredDistanceTo(getTarget()) > 4.0D;
			} else {
				return false;
			}
		}

		@Override
		public boolean shouldContinue() {
			return getMoveControl().isMoving() && isCharging() && getTarget() != null && getTarget().isAlive();
		}

		@Override
		public void start() {
			LivingEntity livingEntity = getTarget();
			Vec3d vec3d = livingEntity.getCameraPosVec(1.0F);
			moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
			setCharging(true);
			playSound(MMSounds.ENTITY_HARROW_CHARGE, 1.0F, 1.0F);
		}


		@Override
		public void stop() {
			setCharging(false);
		}

		@Override
		public void tick() {
			LivingEntity livingEntity = getTarget();
			if (getBoundingBox().intersects(livingEntity.getBoundingBox())) {
				tryAttack(livingEntity);
				setCharging(false);
			} else {
				double d = squaredDistanceTo(livingEntity);
				if (d < 9.0D) {
					Vec3d vec3d = livingEntity.getCameraPosVec(1.0F);
					moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
				}
			}

		}
	}

	class HarrowMoveControl extends MoveControl {

		public HarrowMoveControl(HarrowEntity owner) {
			super(owner);
		}

		@Override
		public void tick() {
			if (this.state == State.MOVE_TO) {
				Vec3d vec3d = new Vec3d(this.targetX - getX(), this.targetY - getY(), this.targetZ - getZ());
				double d = vec3d.length();
				if (d < getBoundingBox().getAverageSideLength()) {
					this.state = State.WAIT;
					setVelocity(getVelocity().multiply(0.5D));
				} else {
					setVelocity(getVelocity().add(vec3d.multiply(this.speed * 0.05D / d)));
					if (getTarget() == null) {
						Vec3d vec3d2 = getVelocity();
						setYaw(-((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F);
					} else {
						double e = getTarget().getX() - getX();
						double f = getTarget().getZ() - getZ();
						setYaw(-((float) MathHelper.atan2(e, f)) * 57.295776F);
					}
					setBodyYaw(getYaw());
				}

			}
		}
	}
}

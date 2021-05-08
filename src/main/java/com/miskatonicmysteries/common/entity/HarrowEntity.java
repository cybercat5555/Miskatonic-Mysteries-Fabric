package com.miskatonicmysteries.common.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class HarrowEntity extends PathAwareEntity { //mostly copies Vex code
    protected static final TrackedData<Byte> HARROW_FLAGS;
    private LivingEntity owner;

    private boolean alive;
    private int lifeTicks;

    public HarrowEntity(EntityType<? extends HarrowEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new HarrowMoveControl(this);
        this.experiencePoints = 3;
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        super.move(type, movement);
        this.checkBlockCollision();
    }
    
    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        this.noClip = false;
        this.setNoGravity(true);
        if (this.alive && --this.lifeTicks <= 0) {
            this.lifeTicks = 20;
            this.damage(DamageSource.STARVE, 1.0F);
        }

    }
    
    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HarrowEntity.ChargeTargetGoal());
        this.goalSelector.add(2, new HarrowEntity.LookAtTargetGoal());
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.add(4, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.add(1, new HarrowEntity.TrackOwnerTargetGoal(this));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, PlayerEntity.class, 10, false, false, (living) -> getOwner() == null));
    }
    
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HARROW_FLAGS, (byte)0);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return super.tryAttack(target);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        if (tag.contains("LifeTicks")) {
            this.setLifeTicks(tag.getInt("LifeTicks"));
        }
    }
    
    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        if (this.alive) {
            tag.putInt("LifeTicks", this.lifeTicks);
        }
    }

    public LivingEntity getOwner() {
        return this.owner;
    }

    private boolean areFlagsSet(int mask) {
        int i = this.dataTracker.get(HARROW_FLAGS);
        return (i & mask) != 0;
    }

    private void setHarrowFlag(int mask, boolean value) {
        int i = this.dataTracker.get(HARROW_FLAGS);
        if (value) {
            i |= mask;
        } else {
            i &= ~mask;
        }

        this.dataTracker.set(HARROW_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.areFlagsSet(1);
    }

    public void setCharging(boolean charging) {
        this.setHarrowFlag(1, charging);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = owner;
    }

    public void setLifeTicks(int lifeTicks) {
        this.alive = true;
        this.lifeTicks = lifeTicks;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VEX_HURT;
    }

    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        this.initEquipment(difficulty);
        this.updateEnchantments(difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    static {
        HARROW_FLAGS = DataTracker.registerData(HarrowEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    class TrackOwnerTargetGoal extends TrackTargetGoal {
        private final TargetPredicate TRACK_OWNER_PREDICATE = (new TargetPredicate()).includeHidden().ignoreDistanceScalingFactor();

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
            this.setControls(EnumSet.of(Goal.Control.MOVE));
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
            for(int i = 0; i < 3; ++i) {
                BlockPos blockPos2 = blockPos.add(random.nextInt(15) - 7, random.nextInt(11) - 5, random.nextInt(15) - 7);
                if (world.isAir(blockPos2)) {
                    moveControl.moveTo((double)blockPos2.getX() + 0.5D, (double)blockPos2.getY() + 0.5D, (double)blockPos2.getZ() + 0.5D, 0.25D);
                    if (getTarget() == null) {
                        getLookControl().lookAt((double)blockPos2.getX() + 0.5D, (double)blockPos2.getY() + 0.5D, (double)blockPos2.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class ChargeTargetGoal extends Goal {
        public ChargeTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.MOVE));
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
            playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
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
            if (this.state == MoveControl.State.MOVE_TO) {
                Vec3d vec3d = new Vec3d(this.targetX - getX(), this.targetY - getY(), this.targetZ - getZ());
                double d = vec3d.length();
                if (d < getBoundingBox().getAverageSideLength()) {
                    this.state = MoveControl.State.WAIT;
                    setVelocity(getVelocity().multiply(0.5D));
                } else {
                    setVelocity(getVelocity().add(vec3d.multiply(this.speed * 0.05D / d)));
                    if (getTarget() == null) {
                        Vec3d vec3d2 = getVelocity();
                        yaw = -((float) MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F;
                    } else {
                        double e = getTarget().getX() - getX();
                        double f = getTarget().getZ() - getZ();
                        yaw = -((float)MathHelper.atan2(e, f)) * 57.295776F;
                    }
                    bodyYaw = yaw;
                }

            }
        }
    }
}

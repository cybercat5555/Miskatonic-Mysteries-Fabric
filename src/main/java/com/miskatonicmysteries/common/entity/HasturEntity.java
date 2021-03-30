package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncHeldEntityPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestStorage;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HasturEntity extends PathAwareEntity implements IAnimatable, Affiliated, EntityHolder {
    public static final TrackedData<Boolean> ACTIVE = DataTracker.registerData(HasturEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Float> PHASING_PROGRESS = DataTracker.registerData(HasturEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Float> HOLDING_PROGRESS = DataTracker.registerData(HasturEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Boolean> HAND_MOVEMENT = DataTracker.registerData(HasturEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected Entity heldEntity;

    private @Nullable
    BlockPos homePos = null;
    private int lastChangedStateTicks;
    private static final int ticksTillAction = 100; //todo 100 for testing purposes //4800;

    private final AnimationFactory factory = new AnimationFactory(this);

    public HasturEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        ignoreCameraFrustum = true;
        setHomePos(getBlockPos());
        //teleportation for movement
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(PHASING_PROGRESS, 1F);
        dataTracker.startTracking(ACTIVE, false);
        dataTracker.startTracking(HOLDING_PROGRESS, 0F);
        dataTracker.startTracking(HAND_MOVEMENT, false);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return super.interactMob(player, hand);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void tickMovement() {
        heldEntity = world.getClosestEntity(LivingEntity.class, new TargetPredicate().setBaseMaxDistance(20), HasturEntity.this, getX(), getY(), getZ(), getBoundingBox().expand(5, 5, 5));
        if (heldEntity != null) {
            if (!world.isClient) {
                SyncHeldEntityPacket.send(this);
            }
            if (heldEntity instanceof LivingEntity) {
                ((LivingEntity) heldEntity).addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 0));
            }
            if (getHoldingProgress() < 1 && isMovingHandUp()) {
                //this works, now do something to allow longer holding, probably count the holding ticks and increase those instead, extrapolate the height from that
                //also get the held entity from the ai actually etc. and handle all that stuff properly
                Vec3d heldPosition = Util.getYawRelativePos(getPos(), 4, headYaw, 0);
                heldEntity.updatePosition(heldPosition.getX(), heldPosition.getY() + 4F * getHoldingProgress(), heldPosition.getZ());
                heldEntity.fallDistance = 0;
                setHoldingProgress(getHoldingProgress() + 0.016025642F);
                if (getHoldingProgress() >= 1) {
                    setHandMovement(false);
                }
            } else if (getHoldingProgress() > 0) {
                setHoldingProgress(getHoldingProgress() - 0.016025642F);
                if (getHoldingProgress() <= 0 && !world.isClient) {
                    heldEntity = null;
                    SyncHeldEntityPacket.send(this);
                }
            }
        } else {
            setHoldingProgress(0);
        }
        super.tickMovement();
    }

    @Override
    protected void initGoals() {
        //redo movement, simus will instead phase around
        this.goalSelector.add(0, new FindHomeGoal());
        this.goalSelector.add(1, new HoldTargetGoal());
        this.goalSelector.add(2, new ChangeActiveStateGoal());
        this.goalSelector.add(3, new PhaseAroundGoal());
        super.initGoals();
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        fallDistance = 0;
    }

    @Override
    public boolean isAffectedBySplashPotions() {
        return false;
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return world.getEntitiesByClass(HasturEntity.class, getBoundingBox().expand(50, 50, 50), null).size() < 1;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    public <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
        if (getHoldingProgress() > 0) { //add crushing animation
            if (isMovingHandUp()) {
       /*         if (getHoldingProgress() >= 1) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_hold", true));
                } else {*/
                event.getController().setAnimation(new AnimationBuilder().addAnimation("active_pickup", false));
                //}
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("active_putdown", false));
            }
        } else if (isActive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("active", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_elaborate", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        setHomePos(getBlockPos());
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }


    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        if (getHomePos() != null) {
            tag.putLong(Constants.NBT.POSITION, getHomePos().asLong());
        }
        tag.putBoolean(Constants.NBT.ACTIVE, isActive());
        tag.putFloat(Constants.NBT.PHASING_PROGRESS, getPhasingProgress());
        tag.putInt(Constants.NBT.LAST_CHANGED, lastChangedStateTicks);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        if (tag.contains(Constants.NBT.POSITION)) {
            setHomePos(BlockPos.fromLong(tag.getLong(Constants.NBT.POSITION)));
        }
        setActive(tag.getBoolean(Constants.NBT.ACTIVE));
        setPhasingProgress(tag.getFloat(Constants.NBT.PHASING_PROGRESS));
        lastChangedStateTicks = tag.getInt(Constants.NBT.LAST_CHANGED);
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return MMAffiliations.HASTUR;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }

    public boolean isActive() {
        return dataTracker.get(ACTIVE);
    }

    public void setActive(boolean active) {
        dataTracker.set(ACTIVE, active);
    }

    public void setPhasingProgress(float progress) {
        dataTracker.set(PHASING_PROGRESS, progress);
    }

    public float getPhasingProgress() {
        return dataTracker.get(PHASING_PROGRESS);
    }

    public BlockPos getHomePos() {
        return homePos;
    }

    public void setHomePos(BlockPos home) {
        this.homePos = home;
    }

    public float getHoldingProgress() {
        return dataTracker.get(HOLDING_PROGRESS);
    }

    public void setHoldingProgress(float progress) {
        dataTracker.set(HOLDING_PROGRESS, progress);
    }

    public boolean isMovingHandUp() {
        return dataTracker.get(HAND_MOVEMENT);
    }

    public void setHandMovement(boolean up) {
        dataTracker.set(HAND_MOVEMENT, up);
    }

    @Override
    public Entity getHeldEntity() {
        return heldEntity;
    }

    @Override
    public void setHeldEntity(Entity entity) {
        this.heldEntity = entity;
    }

    public class ChangeActiveStateGoal extends Goal {

        @Override
        public boolean canStart() {
            if (lastChangedStateTicks++ > ticksTillAction && random.nextFloat() < 0.01F) {
                if (!isActive()) {
                    lastChangedStateTicks = 0;
                    return true;
                } else if (homePos != null) {
                    lastChangedStateTicks = 0;
                    return squaredDistanceTo(homePos.getX(), homePos.getY(), homePos.getZ()) < 1024;
                }
            }
            return false;
        }

        public void start() {
            setActive(!isActive());
        }

        @Override
        public boolean shouldContinue() {
            return false;
        }
    }

    public class FindHomeGoal extends Goal {
        @Override
        public boolean canStart() {
            if (isActive()) {
                if (homePos == null) {
                    return true;
                }
                BlockState home = world.getBlockState(getHomePos());
                return !(home instanceof Affiliated) || !((Affiliated) home).getAffiliation(false).equals(getAffiliation(false));
            }
            return false;
        }

        @Override
        public void tick() {
            List<BlockPos> list = this.getNearbyHomes();
            if (!list.isEmpty()) {
                homePos = list.get(0);
            }
        }

        private List<BlockPos> getNearbyHomes() {
            BlockPos blockPos = getBlockPos();
            PointOfInterestStorage pointOfInterestStorage = ((ServerWorld) world).getPointOfInterestStorage();
            Stream<PointOfInterest> stream = pointOfInterestStorage.getInCircle((pointOfInterestType) -> pointOfInterestType == MMEntities.HASTUR_POI, blockPos, 120, PointOfInterestStorage.OccupationStatus.ANY);
            return stream.map(PointOfInterest::getPos).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.getSquaredDistance(blockPos))).collect(Collectors.toList());
        }
    }

    public class PhaseAroundGoal extends Goal {
        private Vec3d target = null;
        private boolean teleported = false;

        public PhaseAroundGoal() {
            super();
            setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (getHoldingProgress() <= 0 && isActive()) {
                target = getWarpTarget();
                return target != null;
            }
            return false;
        }

        @Override
        public boolean shouldContinue() {
            return target != null;
        }

        @Override
        public void stop() {
            setPhasingProgress(0);
            target = null;
            teleported = false;
        }

        @Override
        public void tick() {
            if (target != null) {
                if (!teleported && squaredDistanceTo(target) > 16) {
                    setPhasingProgress(getPhasingProgress() + 0.01F);
                    if (getPhasingProgress() >= 1) {
                        updatePositionAndAngles(target.x, target.y, target.z, yaw + 90, pitch);
                        teleported = true;
                    }
                } else if (teleported || getPhasingProgress() > 0) {
                    setPhasingProgress(getPhasingProgress() - 0.01F);
                    if (getPhasingProgress() <= 0) {
                        stop();
                        setActive(false);
                    }
                }
            }
        }

        protected @Nullable
        Vec3d getWarpTarget() {
            if (homePos != null) {
                Vec3d pos = new Vec3d(homePos.getX(), homePos.getY(), homePos.getZ());
                if (homePos.getSquaredDistance(getBlockPos()) > 1024) {
                    return TargetFinder.findGroundTargetTowards(HasturEntity.this, 30, 5, 2, pos, Math.PI);
                }
            } //maybe get a certain minimal distance also increase horizontal distance
            return TargetFinder.findTarget(HasturEntity.this, 18, 4);
        }
    }

    public class HoldTargetGoal extends Goal {
        public HoldTargetGoal() {
            super();
            setControls(EnumSet.of(Control.TARGET));
        }

        @Override
        public boolean canStart() {
            return /*isActive() && */heldEntity == null;
        }

        @Override
        public void start() {
            //find any living entity, probably based on how close it is to the "hand"
            heldEntity = getGrabEntity();
            SyncHeldEntityPacket.send(HasturEntity.this);
            setHandMovement(true);
            setHoldingProgress(0);
            super.start();
        }

        @Nullable
        protected LivingEntity getGrabEntity() {
            // Vec3d position = Util.getYawRelativePos(getPos(), 4, headYaw, 0);
            return world.getClosestEntity(PathAwareEntity.class, TargetPredicate.DEFAULT, HasturEntity.this, getX(), getY(), getZ(), getBoundingBox().expand(10, 10, 10));
        }
    }
}

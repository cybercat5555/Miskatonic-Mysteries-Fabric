package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HasturEntity extends PathAwareEntity implements IAnimatable, Affiliated {
    public static final TrackedData<Boolean> ACTIVE = DataTracker.registerData(HasturEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private @Nullable
    BlockPos homePos = null;
    private int lastChangedStateTicks;
    private static final int ticksTillAction = 100; //100 for testing purposes //4800;

    private final AnimationFactory factory = new AnimationFactory(this);

    public HasturEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        ignoreCameraFrustum = true;
        setHomePos(getBlockPos());
        //oh no i need to make a proper navigator for these alas i must code
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(ACTIVE, false);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public void tick() {
        super.tick();
        this.updateFloating();
    }

    private void updateFloating() {
        if (isFullySubmergedInSolid()) {
            onGround = true;
            this.setVelocity(this.getVelocity().add(0, 0.1, 0));
        } else if (onGround) {
            this.setVelocity(this.getVelocity().x, 0, this.getVelocity().z);
        }
    }

    private boolean isFullySubmergedInSolid() { //todo proper collision logic :(
        checkBlockCollision();
        BlockPos blockPos = new BlockPos(getBoundingBox().minX + 0.001D, getBoundingBox().minY + 0.001D, getBoundingBox().minZ + 0.001D);
        BlockPos blockPos2 = new BlockPos(getBoundingBox().maxX - 0.001D, getBoundingBox().minY - 0.001D, getBoundingBox().maxZ - 0.001D);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        checkBlockCollision();
        if (this.world.isRegionLoaded(blockPos, blockPos2)) {
            for (int i = blockPos.getX(); i <= blockPos2.getX(); ++i) {
                for (int j = blockPos.getY(); j <= blockPos2.getY(); ++j) {
                    for (int k = blockPos.getZ(); k <= blockPos2.getZ(); ++k) {
                        mutable.set(i, j, k);
                        BlockState blockState = this.world.getBlockState(mutable);
                        if (!blockState.isSolidBlock(world, mutable)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void tickMovement() {
        noClip = true;
        super.tickMovement();
        noClip = false;
    }

    @Override
    protected void initGoals() {
        //redo movement, simus will instead phase around
        this.goalSelector.add(0, new FindHomeGoal());
        this.goalSelector.add(1, new ChangeActiveStateGoal());
        this.goalSelector.add(3, new WanderAroundHomeGoal(this));
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
        if (isActive()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("active", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_elaborate", true));
        }
        return PlayState.CONTINUE;
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
        tag.putInt(Constants.NBT.LAST_CHANGED, lastChangedStateTicks);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        if (tag.contains(Constants.NBT.POSITION)) {
            setHomePos(BlockPos.fromLong(tag.getLong(Constants.NBT.POSITION)));
        }
        setActive(tag.getBoolean(Constants.NBT.ACTIVE));
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

    public BlockPos getHomePos() {
        return homePos;
    }

    public void setHomePos(BlockPos home) {
        this.homePos = home;
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

    public class WanderAroundHomeGoal extends WanderAroundGoal {
        public WanderAroundHomeGoal(PathAwareEntity pathAwareEntity) {
            super(pathAwareEntity, 1, 100);
        }

        @Override
        public boolean canStart() {
            return isActive() && super.canStart();
        }

        @Override
        protected @Nullable
        Vec3d getWanderTarget() {
            if (homePos != null) {
                Vec3d pos = new Vec3d(homePos.getX(), homePos.getY(), homePos.getZ());
                if (homePos.getSquaredDistance(getBlockPos()) > 1024) {
                    return TargetFinder.findGroundTargetTowards(HasturEntity.this, 30, 5, 2, pos, Math.PI);
                }
            }
            return TargetFinder.findTarget(HasturEntity.this, 18, 4);
        }
    }
}

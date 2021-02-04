package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.entity.ai.FloatyWanderAroundGoal;
import com.miskatonicmysteries.common.feature.interfaces.Resonating;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PhantasmaEntity extends PathAwareEntity implements IAnimatable, Resonating, Flutterer {
    public static final TrackedData<Float> RESONANCE = DataTracker.registerData(PhantasmaEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final TrackedData<Integer> VARIANT = DataTracker.registerData(PhantasmaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private AnimationFactory factory = new AnimationFactory(this);

    public PhantasmaEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        moveControl = new FlightMoveControl(this, 20, true);
    }

    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0F : 1.0F;
    }

    @Override
    public void tick() {
        noClip = true;
        super.tick();
        noClip = false;
        setNoGravity(true);
        if (age > 100) {
            setResonance(getResonance() - 0.005F);
            if (getResonance() <= 0) {
                remove();
            }
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {

    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        setResonance(Math.max(world.getRandom().nextFloat(), 0.2F));
        dataTracker.set(VARIANT, world.getRandom().nextInt(3));
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.0D));
        this.goalSelector.add(2, new FloatyWanderAroundGoal(this, 20));
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        super.initGoals();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    private <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }


    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putFloat(Constants.NBT.RESONANCE, getResonance());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        setResonance(tag.getFloat(Constants.NBT.RESONANCE));
    }


    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(RESONANCE, 0F);
        this.dataTracker.startTracking(VARIANT, 0);
    }

    @Override
    public float getResonance() {
        return dataTracker.get(RESONANCE);
    }

    @Override
    public void setResonance(float resonance) {
        dataTracker.set(RESONANCE, resonance);
    }

    public int getVariant() {
        return dataTracker.get(VARIANT);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation flightNavigation = new BirdNavigation(this, world) {
            public boolean isValidPosition(BlockPos pos) {
                return true;
            }
        };
        flightNavigation.setCanPathThroughDoors(false);
        flightNavigation.setCanSwim(false);
        flightNavigation.setCanEnterOpenDoors(true);
        return flightNavigation;
    }
}

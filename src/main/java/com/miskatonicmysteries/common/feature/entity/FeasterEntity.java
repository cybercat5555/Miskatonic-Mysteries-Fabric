package com.miskatonicmysteries.common.feature.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Predicate;

public class FeasterEntity extends HostileEntity implements IAnimatable {
    private static final TrackedData<Boolean> IS_FLYING = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_GRABBING = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_MELEE = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_RANGED = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> STORED_XP = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.INTEGER);

    AnimationFactory factory = new AnimationFactory(this);
    private final ServerBossBar bossBar;
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = (entity) -> entity.getGroup() != EntityGroup.UNDEAD && entity.isMobOrPlayer();

    public FeasterEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.experiencePoints = 50;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 100).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32F).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(5, new FlyGoal(this, 1.0D));
        this.goalSelector.add(1, new FeasterMeleeAttackGoal());
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class,
        10, false, false, CAN_ATTACK_PREDICATE));
    }

    @Override
    protected void mobTick() {
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        super.mobTick();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(IS_FLYING, true);
        this.dataTracker.startTracking(IS_GRABBING, false);
        this.dataTracker.startTracking(IS_MELEE, false);
        this.dataTracker.startTracking(IS_RANGED, false);
        this.dataTracker.startTracking(STORED_XP, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }

    }
    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }


    private <T extends IAnimatable> PlayState flyStatePredicate(AnimationEvent<T> event) {
        final AnimationController animationController = event.getController();
        AnimationBuilder builder = new AnimationBuilder();
        if(this.dataTracker.get(IS_FLYING)){
            if((this.dead || this.getHealth() < 0.01 || this.isDead())){
                builder.addAnimation("animation.feaster.death_flight", false);
            }else{
                builder.addAnimation("animation.feaster.idle_flight", true);
            }
        }else{
            if((this.dead || this.getHealth() < 0.01 || this.isDead())){
                builder.addAnimation("animation.feaster.death_ground", false);
            }else{
                builder.addAnimation("animation.feaster.idle_ground", true);
            }
        }
        animationController.setAnimation(builder);
        return PlayState.CONTINUE;
    }


    private <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        final AnimationController animationController = event.getController();
        AnimationBuilder builder = new AnimationBuilder();
        if(this.dataTracker.get(IS_FLYING)){
            if(this.dataTracker.get(IS_GRABBING)){
                builder.addAnimation("animation.feaster.grab_flight", false);
                if(true){ //IS GRAB SUCCESSFUL
                    builder.addAnimation("animation.feaster.hold_flight", true);
                }
            }else if(this.dataTracker.get(IS_MELEE)){
                builder.addAnimation("animation.feaster.attack_flight_melee", false);
            }else if(this.dataTracker.get(IS_RANGED)){
                if(true){ //ATTACK TYPE
                    builder.addAnimation("animation.feaster.attack_flight_ranged_lightening", false);
                }else{
                    builder.addAnimation("animation.feaster.attack_flight_ranged_maina", false);
                }

            }
        }
        animationController.setAnimation(builder);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "main_controller", 4, this::animationPredicate));
        animationData.addAnimationController(new AnimationController<>(this, "fly_controller", 4, this::flyStatePredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    class FeasterMeleeAttackGoal extends MeleeAttackGoal {

        public FeasterMeleeAttackGoal() {
            super(FeasterEntity.this, 1.0F, false);
        }

        @Override
        public void start() {
            super.start();
            dataTracker.set(IS_MELEE, true);
        }

        @Override
        public void stop() {
            super.stop();
            dataTracker.set(IS_MELEE, false);
        }

        @Override
        public boolean canStart() {
            return super.canStart();
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return 6;
        }
    }
}

package com.miskatonicmysteries.common.feature.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
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

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class FeasterEntity extends HostileEntity implements IAnimatable {
    private static final TrackedData<Boolean> IS_FLYING = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_GRABBING = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_MELEE = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_RANGED = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> STORED_XP = DataTracker.registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.INTEGER);

    AnimationFactory factory = new AnimationFactory(this);
    private final ServerBossBar bossBar;
    private int GRABCOOLDOWN = 0;
    private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = (entity) -> entity.getGroup() != EntityGroup.UNDEAD && entity.isMobOrPlayer();

    public FeasterEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.bossBar = (ServerBossBar)(new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS)).setDarkenSky(true);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.moveControl = new FeasterMoveControl(this);
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
        this.goalSelector.add(3, new FeasterGrab(this));
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
        nbt.putInt("grabCooldown", this.GRABCOOLDOWN);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
        if(nbt.contains("grabCooldown")){
            this.GRABCOOLDOWN = nbt.getInt("grabCooldown");
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

    @Override
    public void tickMovement() {
        if(GRABCOOLDOWN >= 0){
            GRABCOOLDOWN--;
        }
        super.tickMovement();
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += g - f;
        entity.setYaw(entity.getYaw() + g - f);
        entity.setHeadYaw(entity.getYaw());
    }

    @Override
    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float f = 0.0F;
            float g = (float)((this.isRemoved() ? 0.009999999776482582D : this.getMountedHeightOffset()) + passenger.getHeightOffset());
            if (this.getPassengerList().size() > 1) {
                int i = this.getPassengerList().indexOf(passenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }
                if (passenger instanceof AnimalEntity) {
                    f = (float)((double)f + 0.2D);
                }
            }

            Vec3d i = (new Vec3d(f, 0.0D, 0.0D)).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
            passenger.setPosition(this.getX() + i.x, this.getY() + (double)g, this.getZ() + i.z);
            this.copyEntityData(passenger);
            if (passenger instanceof AnimalEntity && this.getPassengerList().size() > 1) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setBodyYaw(((AnimalEntity)passenger).bodyYaw + (float)j);
                passenger.setHeadYaw(passenger.getHeadYaw() + (float)j);
            }

        }
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

    @Override
    public boolean damage(DamageSource source, float amount) {
        if(this.getFirstPassenger() != null){
            this.getFirstPassenger().stopRiding();
        }
        return super.damage(source, amount);
    }

    class FeasterMoveControl extends MoveControl {
        private final FeasterEntity feasterEntity;
        private int collisionCheckCooldown;

        public FeasterMoveControl(FeasterEntity feasterEntity) {
            super(feasterEntity);
            this.feasterEntity = feasterEntity;
        }

        @Override
        public void tick() {
            if (this.state == State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.feasterEntity.getRandom().nextInt(5) + 2;
                    Vec3d vec3d = new Vec3d(this.targetX - this.feasterEntity.getX(), this.targetY - this.feasterEntity.getY(), this.targetZ - this.feasterEntity.getZ());
                    double d = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        this.feasterEntity.setVelocity(this.feasterEntity.getVelocity().add(vec3d.multiply(0.1D)));
                    } else {
                        this.state = State.WAIT;
                    }
                }

            }
        }


        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.feasterEntity.getBoundingBox();
            for(int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.feasterEntity.world.isSpaceEmpty(this.feasterEntity, box)) {
                    return false;
                }
            }

            return true;
        }
    }

    private static class FlyRandomlyGoal extends Goal {
        private final FeasterEntity feasterEntity;

        public FlyRandomlyGoal(FeasterEntity feasterEntity) {
            this.feasterEntity = feasterEntity;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            MoveControl moveControl = this.feasterEntity.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            } else {
                double d = moveControl.getTargetX() - this.feasterEntity.getX();
                double e = moveControl.getTargetY() - this.feasterEntity.getY();
                double f = moveControl.getTargetZ() - this.feasterEntity.getZ();
                double g = d * d + e * e + f * f;
                return g < 1.0D || g > 3600.0D;
            }
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            Random random = this.feasterEntity.getRandom();
            double d = this.feasterEntity.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double e = this.feasterEntity.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double f = this.feasterEntity.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.feasterEntity.getMoveControl().moveTo(d, e, f, 1.0D);
        }
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

    class FeasterGrab extends Goal{
        private final FeasterEntity feasterEntity;

        FeasterGrab(FeasterEntity feasterEntity) {
            this.feasterEntity = feasterEntity;
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.feasterEntity.getTarget();
            return livingEntity != null && feasterEntity.distanceTo(livingEntity) < 5 && feasterEntity.GRABCOOLDOWN <= 1;
        }

        @Override
        public void start() {
            LivingEntity livingEntity = this.feasterEntity.getTarget();
            if (livingEntity != null) {
                feasterEntity.GRABCOOLDOWN = 200;
                livingEntity.startRiding(feasterEntity);
            }
            super.start();
        }
    }

    class FeasterSucc extends Goal{
        private final LivingEntity livingEntity;

        FeasterSucc(LivingEntity livingEntity) {
            this.livingEntity = livingEntity;
        }

        @Override
        public boolean canStart() {
            return dataTracker.get(IS_GRABBING);
        }

        @Override
        public void tick() {
            super.tick();

        }
    }
}

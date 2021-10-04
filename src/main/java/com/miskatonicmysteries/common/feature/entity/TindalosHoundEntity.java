package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.common.feature.world.MMWorldState;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCache;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.*;

public class TindalosHoundEntity extends HostileEntity implements IAnimatable {
    private static final PathNodeNavigator PHASING_NAVIGATION = new PathNodeNavigator(new LandPathNodeMaker(), 12);

    private static final TrackedData<Optional<UUID>> HARD_TARGET = DataTracker
            .registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Integer> PHASING_PROGRESS = DataTracker
            .registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Direction> PHASING_DIRECTION = DataTracker
            .registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.FACING);
    private static final TrackedData<Optional<BlockPos>> PHASING_DESTINATION = DataTracker
            .registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_POS);
    private static final TrackedData<Byte> HOUND_DATA = DataTracker
            .registerData(TindalosHoundEntity.class, TrackedDataHandlerRegistry.BYTE);

    private final AnimationFactory factory = new AnimationFactory(this);

    public TindalosHoundEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32F);
    }

    public static Optional<Pair<BlockPos, Direction>> findPhasingBlock(LivingEntity target) {
        World world = target.getEntityWorld();
        Set<BlockPos> qualifiedPositions = new HashSet<>();
        for (BlockPos blockPos : BlockPos.iterateOutwards(target.getBlockPos(), 8, 2, 8)) {
            BlockState state = world.getBlockState(blockPos);
            if (state.isAir()) {
                for (Direction direction : Direction.values()) {
                    BlockPos offsetPos = blockPos.offset(direction);
                    if (world.getBlockState(offsetPos).isFullCube(world, offsetPos)) {
                        qualifiedPositions.add(new BlockPos(blockPos));
                    }
                }
            }
        }
        BlockPos pos = target.getBlockPos();
        Path path;
        if (target instanceof MobEntity m) {
            path = PHASING_NAVIGATION.findPathToAny(
                    new ChunkCache(world, pos.add(-8, -8, -8), pos.add(8, 8, 8)),
                    m, qualifiedPositions, 10F, 0, 1);
        } else {
            TindalosHoundEntity dummyHound = new TindalosHoundEntity(MMEntities.TINDALOS_HOUND, world);
            dummyHound.setPosition(target.getPos());
            path = PHASING_NAVIGATION.findPathToAny(new ChunkCache(world, pos.add(-8, -8, -8), pos
                    .add(8, 8, 8)), dummyHound, qualifiedPositions, 10F, 0, 1);
        }
        if (path != null) {
            BlockPos targetPos = path.getTarget();
            List<Direction> possibleDirections = new ArrayList<>();
            for (Direction offset : Direction.values()) {
                BlockPos offsetPos = targetPos.offset(offset);
                if (world.getBlockState(offsetPos).isFullCube(world, offsetPos)) {
                    possibleDirections.add(offset.getOpposite());
                }
            }
            return possibleDirections.size() > 0 ? Optional.of(
                    Pair.of(targetPos, possibleDirections.get(target.getRandom().nextInt(possibleDirections.size())))
            ) : Optional.empty();
        }
        return Optional.empty();
    }

    public static void handleSpawning(PlayerEntity playerEntity) {
        if (!playerEntity.world.isClient && playerEntity.age % 20 == 0) {
            if (!MMWorldState.get(playerEntity.world).getHoundState(playerEntity.getUuid())) {
                spawnFor(playerEntity);
            }
        }
    }

    public static void spawnFor(PlayerEntity playerEntity) {
        TindalosHoundEntity hound = MMEntities.TINDALOS_HOUND.create(playerEntity.world);
        findPhasingBlock(playerEntity).ifPresent(location -> {
            hound.startPhasingTo(location.getFirst(), location.getSecond(), false);
            hound.setPos(location.getFirst().getX(), location.getFirst().getY(), location.getFirst().getZ());
            hound.setTarget(playerEntity);
            hound.setHardTargetUUID(playerEntity.getUuid());
            if (playerEntity.world.spawnEntity(hound)) {
                MMWorldState.get(playerEntity.world).markHoundState(playerEntity.getUuid(), true);
            }
        });
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new HoundDrainTargetGoal());
        this.goalSelector.add(1, new HoundPounceAtTargetGoal());
        this.goalSelector.add(1, new HoundMeleeAttackGoal());
        this.goalSelector.add(2, new PhaseToTargetGoal());
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class,
                10, false, false,
                (entity) -> getHardTargetUUID().map(uuid -> uuid.equals(entity.getUuid())).orElse(true)));
    }

    @Override
    public void tick() {
        super.tick();
        if (world.isClient && random.nextFloat() < 0.1F) {
            world.addParticle(MMParticles.WEIRD_CUBE, getParticleX(1), getRandomBodyY(), getParticleZ(1),
                    MathHelper.nextGaussian(random, 0, 0.01F),
                    MathHelper.nextGaussian(random, 0, 0.01F),
                    MathHelper.nextGaussian(random, 0, 0.01F));
        }

        int phasingProgress = getPhasingProgress();
        if (phasingProgress > 0) {
            if (phasingProgress >= 100) {
                if (world.isClient && phasingProgress % 2 == 0) {
                    spawnPhasingParticles(getParticleX(1), getRandomBodyY(), getParticleZ(1), 0, 0, 0);
                }
            } else {
                getPhasingDestination().ifPresent(destination -> {
                    Direction direction = getPhasingDirection();
                    if (world.isClient && phasingProgress % 2 == 0) {
                        spawnPhasingParticles(
                                destination.getX() + random.nextFloat() * 1.3 * (1 - Math.abs(direction.getOffsetX()))
                                        + (direction.getOffsetX() < 0 ? 1 : 0),
                                destination.getY() + random.nextFloat() * 1.3 * (1 - Math.abs(direction.getOffsetY()))
                                        + (direction.getOffsetY() < 0 ? 1 : 0),
                                destination.getZ() + random.nextFloat() * 1.3 * (1 - Math.abs(direction.getOffsetZ()))
                                        + (direction.getOffsetZ() < 0 ? 1 : 0),
                                direction.getOffsetX() * random.nextFloat() * 0.03F,
                                direction.getOffsetY() * random.nextFloat() * 0.03F,
                                direction.getOffsetZ() * random.nextFloat() * 0.03F
                        );
                    }
                    if (!world.isClient) {
                        if (phasingProgress == 50) {
                            setPos(destination.getX(), destination.getY(), destination.getZ());
                        } else if (phasingProgress == 1){
                            setVelocity(direction.getOffsetX() * 0.5F, direction.getOffsetY() * 0.5F, direction
                                    .getOffsetZ() * 0.5F);
                            velocityModified = true;
                            world.playSound(null, destination, MMSounds.BROKE_VEIL_SPAWN, SoundCategory.HOSTILE, MathHelper
                                    .nextFloat(random, 0.8F, 1.2F), MathHelper.nextFloat(random, 0.7F, 1.4F));
                        }
                    }
                });
            }
            setPhasingProgress(phasingProgress - 1);
        }
    }

    private void spawnPhasingParticles(double x, double y, double z, double velX, double velY, double velZ) {
        world.addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR, x, y, z, velX, velY, velZ);
        if (random.nextBoolean()) {
            world.addParticle(MMParticles.WEIRD_CUBE, x, y, z, velX, velY, velZ);
        }
    }

    @Override
    public void onAttacking(Entity target) {
        super.onAttacking(target);
        if (!world.isClient && target instanceof LivingEntity l) {
            if (random.nextBoolean()) {
                l.addStatusEffect(new StatusEffectInstance(MMStatusEffects.BLEED, 200, 0, false, false, true), this);
            }
            if (random.nextBoolean()) {
                l.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 100, 0), this);
            }
        }
    }

    @Override
    protected boolean isImmobile() {
        return getPhasingProgress() > 0 || super.isImmobile();
    }

    @Override
    public boolean isInvisible() {
        return isDeepPhasing();
    }

    @Override
    public boolean isInvulnerable() {
        return isDeepPhasing();
    }

    private boolean isDeepPhasing() {
        return getPhasingProgress() > 0 && getPhasingProgress() < 100;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HARD_TARGET, Optional.empty());
        this.dataTracker.startTracking(PHASING_PROGRESS, 0);
        this.dataTracker.startTracking(PHASING_DESTINATION, Optional.empty());
        this.dataTracker.startTracking(PHASING_DIRECTION, Direction.UP);
        this.dataTracker.startTracking(HOUND_DATA, (byte) 0);
    }

    public Optional<UUID> getHardTargetUUID() {
        return this.dataTracker.get(HARD_TARGET);
    }

    public void setHardTargetUUID(UUID uuid) {
        this.dataTracker.set(HARD_TARGET, Optional.of(uuid));
    }

    public int getPhasingProgress() {
        return this.dataTracker.get(PHASING_PROGRESS);
    }

    public void setPhasingProgress(int phasingProgress) {
        this.dataTracker.set(PHASING_PROGRESS, phasingProgress);
    }

    public Optional<BlockPos> getPhasingDestination() {
        return this.dataTracker.get(PHASING_DESTINATION);
    }

    public void setPhasingDestination(BlockPos phasingDestination) {
        this.dataTracker
                .set(PHASING_DESTINATION, phasingDestination != null ?
                        Optional.of(phasingDestination) :
                        Optional.empty());
    }

    public Direction getPhasingDirection() {
        return this.dataTracker.get(PHASING_DIRECTION);
    }

    public void setPhasingDirection(Direction phasingDirection) {
        this.dataTracker.set(PHASING_DIRECTION, phasingDirection);
    }

    public void startPhasingTo(BlockPos pos, Direction direction, boolean inWorld) {
        setPhasingProgress(inWorld ? 200 : 100);
        setPhasingDestination(pos);
        setPhasingDirection(direction);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid(Constants.NBT.TARGET)) {
            setHardTargetUUID(nbt.getUuid(Constants.NBT.TARGET));
        }
        setPhasingProgress(nbt.getInt(Constants.NBT.PHASE_TICKS));
        if (nbt.contains(Constants.NBT.POSITION)) {
            setPhasingDestination(NbtHelper.toBlockPos((NbtCompound) nbt.get(Constants.NBT.POSITION)));
            setPhasingDirection(Direction.byId(nbt.getInt(Constants.NBT.PHASE_DIRECTION)));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        getHardTargetUUID().ifPresent(targetUUID -> nbt.putUuid(Constants.NBT.TARGET, targetUUID));
        nbt.putInt(Constants.NBT.PHASE_TICKS, getPhasingProgress());
        getPhasingDestination().ifPresent(destination -> {
            nbt.put(Constants.NBT.POSITION, NbtHelper.fromBlockPos(destination));
            nbt.putInt(Constants.NBT.PHASE_DIRECTION, getPhasingDirection().getId());
        });

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "main_controller", 4, this::animationPredicate));

    }

    private <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
        float limbSwingAmount = event.getLimbSwingAmount();
        boolean isMoving = limbSwingAmount > 0.1F;
        if (isPouncing()) {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.tindalos_hound.pounce", true));
        } else if (isSucking()) {
            event.getController()
                    .setAnimation(new AnimationBuilder()
                            .addAnimation("animation.tindalos_hound.attack_drain_loop", true));
        } else if (isMoving) {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.tindalos_hound.walk", true));
        } else if (getPhasingProgress() > 100 || getHardTargetUUID().isPresent()) {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.tindalos_hound.spot", true));
        } else {
            event.getController()
                    .setAnimation(new AnimationBuilder().addAnimation("animation.tindalos_hound.idle", true));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private boolean isPouncing() {
        return (this.dataTracker.get(HOUND_DATA) & 1) != 0;
    }

    private void setPouncing(boolean pounce) {
        byte b = this.dataTracker.get(HOUND_DATA);
        this.dataTracker.set(HOUND_DATA, pounce ? (byte) (b | 1) : (byte) (b & -2));
    }

    private boolean isSucking() {
        return (this.dataTracker.get(HOUND_DATA) & 2) != 0;
    }

    private void setSucking(boolean suck) {
        byte b = this.dataTracker.get(HOUND_DATA);
        this.dataTracker.set(HOUND_DATA, suck ? (byte) (b | 2) : (byte) (b & -3));
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (getTarget() != null) {
            getLookControl().lookAt(getTarget(), 10.0F, 10.0F);
        }
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        if (!world.isClient) {
            getHardTargetUUID().ifPresent(target -> {
                if (reason == RemovalReason.KILLED) {
                    MMWorldState.get(world).removeHoundForUUID(target);
                } else {
                    MMWorldState.get(world).markHoundState(target, false);
                }
            });
        }
    }

    class HoundDrainTargetGoal extends Goal {
        int drainCooldown;
        int suckTicks;

        public HoundDrainTargetGoal() {
            this.setControls(EnumSet.of(Control.LOOK, Goal.Control.MOVE, Control.TARGET));
            this.drainCooldown = 40;
        }

        public boolean canStart() {
            return getTarget() != null && distanceTo(getTarget()) <= 3 && random.nextBoolean() && drainCooldown-- <= 0;
        }

        public boolean shouldContinue() {
            return getHealth() < getMaxHealth() && getTarget() != null && distanceTo(getTarget()) <= 3 && isSucking();
        }

        public void start() {
            setSucking(true);
        }

        @Override
        public void tick() {
            super.tick();
            if (isSucking()) {
                LivingEntity target = getTarget();
                getLookControl().lookAt(target, 10, 10);
                Vec3d pos = Util.getYawRelativePos(getPos(), 2.5, getYaw(), 0);
                Vec3d motionVec = new Vec3d(pos.x - target.getX(), pos.y - target.getY(), pos.z - target.getZ());
                if (motionVec.length() > 0.2F) {
                    motionVec = motionVec.normalize().multiply(0.15F);
                    target.setVelocity(motionVec);
                    target.velocityModified = true;
                    target.velocityDirty = true;
                }
                if (suckTicks % 60 == 0) {
                    if (target.damage(DamageSource.MAGIC, 4)) {    //new damage type?
                        heal(4);
                    }
                }
                suckTicks++;
            } else {
                stop();
            }
        }

        @Override
        public void stop() {
            super.stop();
            setSucking(false);
            drainCooldown = 40;
            suckTicks = 0;
        }
    }

    class HoundPounceAtTargetGoal extends Goal {
        public HoundPounceAtTargetGoal() {
            this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
        }

        public boolean canStart() {
            if (hasPassengers() || isSucking()) {
                return false;
            } else {
                if (getTarget() == null) {
                    return false;
                } else {
                    double d = squaredDistanceTo(getTarget());
                    if (!(d < 4.0D) && !(d > 36.0D)) {
                        if (!isOnGround()) {
                            return false;
                        } else {
                            return getRandom().nextInt(5) == 0;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        public boolean shouldContinue() {
            return !isOnGround();
        }

        public void start() {
            Vec3d velocity = getVelocity();
            Vec3d movement = new Vec3d(getTarget().getX() - getX(), 1.5D, getTarget().getZ() - getZ());
            if (movement.lengthSquared() > 1.0E-7D) {
                movement = movement.normalize().multiply(0.8D).add(velocity.multiply(0.2D));
            }

            setVelocity(movement.x, movement.y, movement.z);
            setPouncing(true);
        }

        @Override
        public void stop() {
            super.stop();
            setPouncing(false);
        }
    }

    class HoundMeleeAttackGoal extends MeleeAttackGoal {
        public HoundMeleeAttackGoal() {
            super(TindalosHoundEntity.this, 1.0F, false);
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !isSucking();
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return 4;
        }
    }

    class PhaseToTargetGoal extends Goal {
        int navigationCheckCooldown;
        int phasingCooldown;

        PhaseToTargetGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return phasingCooldown-- < 0 && isOnGround() && getPhasingProgress() == 0 && (getTarget() == null ? getHardTargetUUID()
                    .isPresent() && isPlayerInRange() : getNavigation()
                    .isIdle() && !canNavigateToEntity(getTarget()));
        }

        private boolean canNavigateToEntity(LivingEntity entity) {
            if (navigationCheckCooldown-- > 0) {
                return true;
            }
            navigationCheckCooldown += 10;
            Path path = getNavigation().findPathTo(entity, 0);
            if (path == null) {
                return false;
            } else {
                PathNode pathNode = path.getEnd();
                if (pathNode == null) {
                    return false;
                } else {
                    int i = pathNode.x - entity.getBlockX();
                    int j = pathNode.z - entity.getBlockZ();
                    return (i * i + j * j) <= 2.25D;
                }
            }
        }

        private boolean isPlayerInRange() {
            PlayerEntity player = world.getPlayerByUuid(getHardTargetUUID().get());
            return player != null && distanceTo(player) > 32;
        }

        @Override
        public void start() {
            LivingEntity target = getTarget();
            if (target == null) {
                target = world.getPlayerByUuid(getHardTargetUUID().get());
            }
            if (target != null) {
                TindalosHoundEntity.findPhasingBlock(target)
                        .ifPresent(pair -> {
                            phasingCooldown = 20;
                            startPhasingTo(pair.getFirst(), pair.getSecond(), true);
                        });
            }
        }
    }
}

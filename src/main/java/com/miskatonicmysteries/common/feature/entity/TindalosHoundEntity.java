package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSounds;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
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
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCache;
import software.bernie.geckolib3.core.IAnimatable;
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

    private final AnimationFactory factory = new AnimationFactory(this);

    public TindalosHoundEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    //check ticks without hard target
    //if it has none, remove the hound and add it to internal storage
    //internal storage associates one player with hound data, when player ticks, give chance for hound to spawn
    //spawning the hound that way removes it from the data (it may return later)
    //a hound spawned that way will enter phase mode, that means it starts invisible (also means: phasing ticks start at half the amount)
    //phasing: measured in ticks: 200 ticks; first 100 ticks are spent disappearing somehow (that is just fading away with smoke and dripping particles)
    //last 100 ticks cause particles to spawn at the target location on the target side (maybe special phase target data)
    //after the 100 ticks, the hound will suddenly pop up and attack
    //phasing may also occur if the target is too far, but the hound is still in place. in that case it starts at 200, not 100 (counting down)
    //while starting phasing, the hound is fully vulnerable

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

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, false));
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
                    if (!world.isClient && phasingProgress == 1) {
                        setPos(destination.getX(), destination.getY(), destination.getZ());
                        setVelocity(direction.getOffsetX() * 0.5F, direction.getOffsetY() * 0.5F, direction.getOffsetZ() * 0.5F);
                        velocityModified = true;
                        world.playSound(null, destination, MMSounds.BROKE_VEIL_SPAWN, SoundCategory.HOSTILE, MathHelper
                                .nextFloat(random, 0.8F, 1.2F), MathHelper.nextFloat(random, 0.7F, 1.4F));
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
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    class PhaseToTargetGoal extends Goal {
        int navigationCheckCooldown;
        int phasingCooldown;

        @Override
        public boolean canStart() {
            return phasingCooldown-- < 0 && getPhasingProgress() == 0 && (getTarget() == null ? getHardTargetUUID()
                    .isPresent() && isPlayerInRange() : getNavigation().isIdle() && !canNavigateToEntity(getTarget()));
        }

        private boolean canNavigateToEntity(LivingEntity entity) {
            if (navigationCheckCooldown-- > 0) {
                return true;
            }
            navigationCheckCooldown += 10;
            Path path = getNavigation().findPathTo(entity, 32);
            if (path == null) {
                return false;
            } else {
                PathNode pathNode = path.getEnd();
                if (pathNode == null) {
                    return false;
                } else {
                    int i = pathNode.x - entity.getBlockX();
                    int j = pathNode.z - entity.getBlockZ();
                    return (double) (i * i + j * j) <= 2.25D;
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
                            phasingCooldown = 200;
                            startPhasingTo(pair.getFirst(), pair.getSecond(), true);
                        });
            }
        }
    }
}

package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
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
import java.util.Optional;
import java.util.UUID;

public abstract class TentacleEntity extends PathAwareEntity implements Affiliated, IAnimatable {
    private static final TrackedData<Optional<UUID>> OWNER = DataTracker.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Optional<UUID>> SPECIFIC_TARGET = DataTracker.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final TrackedData<Boolean> BROAD_SWING = DataTracker.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Float> SIZE = DataTracker.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private final AnimationFactory factory = new AnimationFactory(this);
    private LivingEntity owner;
    private boolean monster = false;
    private int maxAge = 600;

    protected TentacleEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
        if (spawnReason != SpawnReason.MOB_SUMMONED) {
            monster = true;
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(OWNER, Optional.empty());
        dataTracker.startTracking(SPECIFIC_TARGET, Optional.empty());
        dataTracker.startTracking(BROAD_SWING, false);
        dataTracker.startTracking(SIZE, 0F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwingAtTargetGoal());
        this.targetSelector.add(0, new FollowTargetGoal<>(this, LivingEntity.class, 10, false, false, this::isValidTarget));
    }

    protected boolean isValidTarget(LivingEntity target) {
        if (target instanceof TentacleEntity tentacle && tentacle.getOwnerUUID().isEmpty()){
            return false;
        }
        if (getOwnerUUID().isPresent()) {
            if (target.getUuid().equals(getOwnerUUID().get())) {
                if (owner == null) {
                    owner = target;
                }
                return false;
            }
            if (owner != null && (target.equals(owner.getAttacker()) || owner.equals(target.getAttacker()))) {
                return true;
            }
        }
        if (getTargetUUID().isPresent() && getTargetUUID().get().equals(target.getUuid())) {
            return true;
        }
        if (monster && !(target instanceof Monster)) {
            return true;
        }
        Affiliation affiliation = MiskatonicMysteriesAPI.getNonNullAffiliation(target, true);
        if ((!monster && target instanceof Monster) && (getAffiliation(false) == MMAffiliations.NONE || affiliation != getAffiliation(true))) {
            return true;
        }
        return target instanceof ProtagonistEntity;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 5, this::animationPredicate));
    }

    public <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
        if (handSwinging) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(isBroadSwing() ? "attack_spread" : "attack", false));
            return PlayState.CONTINUE;
        }
        if (isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("alert", true));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);
        tag.putBoolean(Constants.NBT.BROAD_SWING, isBroadSwing());
        if (getOwnerUUID().isPresent()) {
            tag.putUuid(Constants.NBT.OWNER, getOwnerUUID().get());
        }
        if (getTargetUUID().isPresent()) {
            tag.putUuid(Constants.NBT.TARGET, getTargetUUID().get());
        }
        tag.putBoolean(Constants.NBT.MONSTER, monster);
        tag.putInt(Constants.NBT.MAX_AGE, maxAge);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);
        dataTracker.set(BROAD_SWING, tag.getBoolean(Constants.NBT.BROAD_SWING));
        dataTracker.set(OWNER, tag.contains(Constants.NBT.OWNER) ? Optional.of(tag.getUuid(Constants.NBT.OWNER)) : Optional.empty());
        dataTracker.set(SPECIFIC_TARGET, tag.contains(Constants.NBT.TARGET) ? Optional.of(tag.getUuid(Constants.NBT.TARGET)) : Optional.empty());
        monster = tag.getBoolean(Constants.NBT.MONSTER);
        maxAge = tag.getInt(Constants.NBT.MAX_AGE);
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        if (type != MovementType.PLAYER) {
            super.move(type, movement);
        }
    }

    @Override
    public void tickMovement() {
        int maxProgress = isBroadSwing() ? 37 : 18;
        int hitTick = isBroadSwing() ? 15 : 10;
        if (this.handSwinging) {
            ++this.handSwingTicks;
            if (handSwingTicks == hitTick) {
                if (getTarget() != null && getTarget().distanceTo(this) <= 3 && isWithinAngle(getTarget())) {
                    tryAttack(getTarget());
                }
            }
            if (this.handSwingTicks >= maxProgress) {
                this.handSwingTicks = 0;
                this.handSwinging = false;
            }
        } else {
            this.handSwingTicks = 0;
        }
        this.handSwingProgress = (float) this.handSwingTicks / maxProgress;
        super.tickMovement();
        this.setVelocity(0, getVelocity().y, 0);
    }

    private boolean isWithinAngle(LivingEntity entity) {
        Vec3d vec = new Vec3d(getX() - entity.getX(), getY() - entity.getY(), getZ() - entity.getZ());
        double targetYaw = -Math.toDegrees(Math.atan2(-vec.x, -vec.z));
        if (targetYaw < 0) {
            targetYaw += 360;
        }
        headYaw %= 360;
        if (headYaw < 0) {
            headYaw += 360;
        }
        return Math.abs(targetYaw - headYaw) < (isBroadSwing() ? 60 : 15);
    }

    @Override
    protected void mobTick() {
        super.mobTick();

        if (age < 20) {
            setSize(getSize() + 0.05F);
        } else if (age > maxAge) {
            setSize(getSize() - 0.05F);
            if (getSize() < 0) {
                remove(RemovalReason.KILLED);
            }
        }
    }

    public float getSize() {
        return dataTracker.get(SIZE);
    }

    public void setSize(float size) {
        dataTracker.set(SIZE, size);
    }

    @Override
    public boolean tryAttack(Entity target) {
        float f = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * (isBroadSwing() ? 1 : 2);
        float g = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK) * (isBroadSwing() ? 2 : 1);
        if (target instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.getMainHandStack(), ((LivingEntity) target).getGroup());
            g += (float) EnchantmentHelper.getKnockback(this);
        }

        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            target.setOnFireFor(i * 4);
        }

        boolean damage = target.damage(DamageSource.mob(this), f);
        if (damage) {
            if (g > 0.0F && target instanceof LivingEntity) {
                ((LivingEntity) target).takeKnockback(g * 0.5F, MathHelper.sin(this.getYaw() * 0.017453292F), (-MathHelper.cos(this.getYaw() * 0.017453292F)));
                this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
            }
            this.applyDamageEffects(this, target);
            this.onAttacking(target);
        }

        return damage;
    }

    @Override
    public void pushAwayFrom(Entity entity) {

    }

    public LivingEntity getOwner() {
        if (owner != null) {
            return owner;
        }
        return world.getPlayerByUuid(getOwnerUUID().get());
    }

    public void setOwner(LivingEntity owner) {
        dataTracker.set(OWNER, Optional.of(owner.getUuid()));
    }

    public boolean isBroadSwing() {
        return dataTracker.get(BROAD_SWING);
    }

    public Optional<UUID> getOwnerUUID() {
        return dataTracker.get(OWNER);
    }

    public Optional<UUID> getTargetUUID() {
        return dataTracker.get(SPECIFIC_TARGET);
    }

    public void setSpecificTarget(LivingEntity target) {
        dataTracker.set(SPECIFIC_TARGET, Optional.of(target.getUuid()));
    }


    @Override
    protected boolean isDisallowedInPeaceful() {
        return monster;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }

    public class SwingAtTargetGoal extends Goal {
        public SwingAtTargetGoal() {
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        public boolean canStart() {
            return getTarget() != null;
        }

        public boolean shouldContinue() {
            if (!canStart()) {
                return false;
            } else {
                LivingEntity target = getTarget();
                return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity) target).isCreative();
            }
        }

        public void start() {
            setAttacking(true);
        }

        public void stop() {
            LivingEntity livingEntity = getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                setTarget(null);
            }
            setAttacking(false);
        }

        public void tick() {
            LivingEntity target = getTarget();
            getLookControl().lookAt(target, 40, 40);
            if (this.getSquaredMaxAttackDistance(target) >= squaredDistanceTo(target) && !handSwinging) {
                dataTracker.set(BROAD_SWING, random.nextBoolean());
                swingHand(Hand.MAIN_HAND);
            }
        }

        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return 13;
        }
    }
}

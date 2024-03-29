package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class TentacleEntity extends PathAwareEntity implements Affiliated, IAnimatable {

	private static final TrackedData<Optional<UUID>> OWNER = DataTracker
		.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	private static final TrackedData<Optional<UUID>> SPECIFIC_TARGET = DataTracker
		.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	private static final TrackedData<Boolean> BROAD_SWING = DataTracker
		.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_SIMPLE = DataTracker
		.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Float> SIZE = DataTracker
		.registerData(TentacleEntity.class, TrackedDataHandlerRegistry.FLOAT);
	private final AnimationFactory factory = new AnimationFactory(this);
	private LivingEntity owner;
	private boolean summoned = true;
	private int maxAge = 600;

	protected TentacleEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwingAtTargetGoal());
		this.targetSelector
			.add(0, new ActiveTargetGoal<>(this, LivingEntity.class, 10, false, false, this::isValidTarget));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(OWNER, Optional.empty());
		dataTracker.startTracking(SPECIFIC_TARGET, Optional.empty());
		dataTracker.startTracking(BROAD_SWING, false);
		dataTracker.startTracking(SIZE, 1F);
		dataTracker.startTracking(IS_SIMPLE, false);
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
		tag.putInt(Constants.NBT.MAX_AGE, maxAge);
		tag.putBoolean(Constants.NBT.SIMPLE, isSimple());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		dataTracker.set(BROAD_SWING, tag.getBoolean(NBT.BROAD_SWING));
		dataTracker
			.set(OWNER, tag.contains(NBT.OWNER) ? Optional.of(tag.getUuid(NBT.OWNER)) : Optional
				.empty());
		dataTracker.set(SPECIFIC_TARGET, tag.contains(NBT.TARGET) ? Optional
			.of(tag.getUuid(NBT.TARGET)) : Optional.empty());
		maxAge = tag.getInt(NBT.MAX_AGE);
		summoned = tag.getBoolean(NBT.SUMMONED);
		setSimple(tag.getBoolean(NBT.SIMPLE));
	}

	@Override
	public void tickMovement() {
		int maxProgress = isBroadSwing() ? 37 : 18;
		int hitTick = isBroadSwing() ? 15 : 10;
		if (this.handSwinging) {
			++this.handSwingTicks;
			if (handSwingTicks == hitTick) {
				if (isSimple()) {
					List<Entity> targets = world.getOtherEntities(this,
																  getBoundingBox().expand(3, 0, 3),
																  (entity) -> entity instanceof LivingEntity && entity != getOwner() &&
																	  (!(entity instanceof TentacleEntity t) || t.getOwner() != getOwner()));
					for (Entity target : targets) {
						if (target.distanceTo(this) <= 3 && isWithinAngle((LivingEntity) target)) {
							tryAttack(target);
						}
					}
				} else {
					if (getTarget() != null && getTarget().distanceTo(this) <= 3 && isWithinAngle(getTarget())) {
						tryAttack(getTarget());
					}
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
		return Math.abs(targetYaw - headYaw) < (isSimple() ? 100 : (isBroadSwing() ? 60 : 15));
	}

	public LivingEntity getOwner() {
		if (owner != null) {
			return owner;
		}
		return getOwnerUUID().isPresent() ? world.getPlayerByUuid(getOwnerUUID().get()) : null;
	}

	public void setOwner(LivingEntity owner) {
		dataTracker.set(OWNER, Optional.of(owner.getUuid()));
	}

	@Override
	protected boolean isDisallowedInPeaceful() {
		return getOwnerUUID().isEmpty();
	}

	@Override
	protected void mobTick() {
		if (!isSimple()) {
			super.mobTick();
		} else if (!handSwinging) {
			swingHand(Hand.MAIN_HAND);
		}

		if (age < (isSimple() ? maxAge / 2 : 20)) {
			setSize(getSize() + 0.05F);
		} else if (summoned && age > maxAge) {
			setSize(getSize() - 0.05F);
			if (getSize() < 0) {
				remove(RemovalReason.KILLED);
			}
		}
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
										   @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		if (spawnReason != SpawnReason.MOB_SUMMONED) {
			summoned = false;
		}
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
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
				((LivingEntity) target)
					.takeKnockback(g * 0.5F, MathHelper.sin(this.getYaw() * 0.017453292F), (-MathHelper
						.cos(this.getYaw() * 0.017453292F)));
				this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
			}
			this.applyDamageEffects(this, target);
			this.onAttacking(target);
		}

		return damage;
	}

	public float getSize() {
		return dataTracker.get(SIZE);
	}

	public void setSize(float size) {
		dataTracker.set(SIZE, size);
	}

	protected boolean isValidTarget(LivingEntity target) {
		if (isSimple()) {
			return false;
		}
		if (getOwnerUUID().isEmpty()) {
			if (target instanceof TentacleEntity) {
				return false;
			}
			Affiliation affiliation = MiskatonicMysteriesAPI.getNonNullAffiliation(target, true);
			if ((getAffiliation(false) == MMAffiliations.NONE || affiliation != getAffiliation(false))) {
				return true;
			}
		} else {
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
		return target instanceof ProtagonistEntity;
	}

	public Optional<UUID> getOwnerUUID() {
		return dataTracker.get(OWNER);
	}

	public Optional<UUID> getTargetUUID() {
		return dataTracker.get(SPECIFIC_TARGET);
	}

	public boolean isSimple() {
		return dataTracker.get(IS_SIMPLE);
	}

	public void setSimple(boolean simple) {
		this.dataTracker.set(IS_SIMPLE, simple);
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
			event.getController().setAnimation(new AnimationBuilder()
												   .addAnimation(isBroadSwing() ? "attack_spread" : "attack", false));
			return PlayState.CONTINUE;
		}
		if (isAttacking()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("alert", true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
		return PlayState.CONTINUE;
	}

	public boolean isBroadSwing() {
		return dataTracker.get(BROAD_SWING);
	}

	public void setBroadSwing(boolean broadSwing) {
		this.dataTracker.set(BROAD_SWING, broadSwing);
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public void move(MovementType type, Vec3d movement) {
		if (type != MovementType.PLAYER) {
			super.move(type, movement);
		}
	}

	@Override
	public void pushAwayFrom(Entity entity) {

	}

	public void setSpecificTarget(LivingEntity target) {
		dataTracker.set(SPECIFIC_TARGET, Optional.of(target.getUuid()));
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
			return !isSimple() && getTarget() != null;
		}

		public boolean shouldContinue() {
			if (!canStart()) {
				return false;
			} else {
				LivingEntity target = getTarget();
				return !(target instanceof PlayerEntity) || !target.isSpectator() && !((PlayerEntity) target)
					.isCreative();
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
				setBroadSwing(random.nextBoolean());
				swingHand(Hand.MAIN_HAND);
			}
		}

		protected double getSquaredMaxAttackDistance(LivingEntity entity) {
			return 13;
		}
	}
}

package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.ai.CastSpellGoal;
import com.miskatonicmysteries.common.feature.entity.navigation.FeasterLogic;
import com.miskatonicmysteries.common.feature.entity.navigation.FeasterMoveController;
import com.miskatonicmysteries.common.feature.entity.navigation.FeasterPathNodeMaker;

import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.ManiaCloudPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.entity.MobEntityAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FeasterEntityOld {}/*extends HostileEntity implements IAnimatable, CastingMob {

	private static final TrackedData<Boolean> IS_FLYING = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.BOOLEAN); //condense into byte flag-attribute
	private static final TrackedData<Boolean> IS_MELEE = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Boolean> IS_RANGED = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Integer> STORED_XP = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.INTEGER); //server-side only, no need to track
	protected static final TrackedData<Integer> CASTING_TIME_LEFT = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final Predicate<LivingEntity> CAN_ATTACK_PREDICATE = (entity) -> !(entity instanceof Affiliated affiliated && affiliated.getAffiliation(false) == MMAffiliations.HASTUR);
	public int navigationType;
	public FeasterMoveController feasterMoveController;
	public FeasterLogic feasterLogic;
	@Nullable
	public Spell currentSpell;
	AnimationFactory factory = new AnimationFactory(this);


	public FeasterEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 50;
		this.feasterMoveController = new FeasterMoveController(this);
		this.feasterLogic = createFeasterLogic();
		changeEntityNavigation(0);

	}

	public void changeEntityNavigation(int navType) {
		if (navType == 0) {
			this.moveControl = new FeasterMoveController.GroundMoveControl(this);
			this.navigation = createNavigation(world, FeasterPathNodeMaker.NavType.WALKING);
			this.navigationType = 0;
			this.setFlying(false);
		} else {
			this.moveControl = new FeasterMoveController.FlightMoveControl(this);
			this.navigation = createNavigation(world, FeasterPathNodeMaker.NavType.FLYING);
			this.navigationType = 1;
			this.setFlying(true);
		}
	}

	protected EntityNavigation createNavigation(World world, FeasterPathNodeMaker.NavType type) {
		FeasterPathNodeMaker newNavigator = new FeasterPathNodeMaker(this, world, type);
		this.navigation = newNavigator;
		newNavigator.setCanSwim(true);
		return newNavigator;
	}

	protected FeasterLogic createFeasterLogic() {
		return new FeasterLogic(this);
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 100)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32F)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579D)
			.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(1, new FeasterSwipeAttackGoal(this));
		this.goalSelector.add(2, new FeasterWanderGoal(this));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new CastSpellGoal<>(this));
		this.goalSelector.add(7, new FeasterLookAtTargetGoal(this));
		this.goalSelector.add(7, new LookAroundGoal(this));
		this.goalSelector.add(7, new FeasterManiaCloudAttackGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class,
														  10, false, false, CAN_ATTACK_PREDICATE));
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return createNavigation(world, FeasterPathNodeMaker.NavType.WALKING);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(IS_FLYING, false);
		this.dataTracker.startTracking(IS_MELEE, false);
		this.dataTracker.startTracking(IS_RANGED, false);
		this.dataTracker.startTracking(STORED_XP, 0);
		dataTracker.startTracking(CASTING_TIME_LEFT, 0);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("Flying", this.isFlying());
		nbt.putInt(Constants.NBT.CASTING, getCastTime());
		if (currentSpell != null) {
			NbtCompound spell = currentSpell.toTag(new NbtCompound());
			nbt.put(Constants.NBT.SPELL, spell);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains(Constants.NBT.SPELL)) {
			currentSpell = Spell.fromTag((NbtCompound) nbt.get(Constants.NBT.SPELL));
		}
		setCastTime(nbt.getInt(Constants.NBT.CASTING));
	}

	@Override
	protected void mobTick() {
		super.mobTick();
		if (!this.isDead()) {
			if (!world.isClient()) {
				this.feasterLogic.updateLogic();
				if (isFlying()) {
					this.feasterMoveController.flyingTick();
				}
				if (isCasting()) {
					if (currentSpell != null) {
						EffectParticlePacket.send(this);
						if (getTarget() == null) {
							setCastTime(0);
						}
					}
				}
			}
		}
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);
		if(target != null){
			feasterMoveController.setFlightTarget(target.getPos());
		}
	}

	public boolean isFlying() {
		return this.dataTracker.get(IS_FLYING);
	}

	public void setFlying(boolean flying) {
		this.dataTracker.set(IS_FLYING, flying);
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.getFirstPassenger() != null) {
			this.getFirstPassenger().stopRiding();
		}
		return super.damage(source, amount);
	}

	public boolean trySwipeAttack(Entity target) {
		float f = (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		float g = (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK);

		boolean randomAttack = this.getRandom().nextBoolean();
		boolean bl;
		if(randomAttack){
			bl = target.damage(Constants.DamageSources.FEASTER, f);
		}else{
			bl = target.damage(DamageSource.mob(this), f);
			((LivingEntity) target).addStatusEffect(new StatusEffectInstance(MMStatusEffects.BLEED, 20 * 2), this);
		}
		((LivingEntity) target).takeKnockback((g * 0.5F),  MathHelper.sin(this.getYaw() * 0.017453292F), (-MathHelper.cos(this.getYaw() * 0.017453292F)));
		this.setVelocity(this.getVelocity().multiply(0.6D, 1.0D, 0.6D));
		if(bl){
			if (g > 0.0F) {
				if (target instanceof PlayerEntity playerEntity) {
					((MobEntityAccessor)this).invokeDisablePlayerShield(playerEntity, this.getMainHandStack(), playerEntity.isUsingItem() ? playerEntity.getActiveItem() : ItemStack.EMPTY);
				}
			}
			this.applyDamageEffects(this, target);
			this.onAttacking(target);
		}
		return bl;
	}

	@Override
	public void registerControllers(AnimationData animationData) {
		animationData.addAnimationController(new AnimationController<>(this, "main_controller", 4, this::animationPredicate));
		animationData.addAnimationController(new AnimationController<>(this, "fly_controller", 4, this::flyStatePredicate));
	}

	private <T extends IAnimatable> PlayState flyStatePredicate(AnimationEvent<T> event) {
		final AnimationController animationController = event.getController();
		AnimationBuilder builder = new AnimationBuilder();
		if (this.dataTracker.get(IS_FLYING)) {
			if ((this.dead || this.getHealth() < 0.01 || this.isDead())) {
				builder.addAnimation("animation.feaster.death_flight", false);
			} else {
				builder.addAnimation("animation.feaster.idle_flight", true);
			}
		} else {
			if ((this.dead || this.getHealth() < 0.01 || this.isDead())) {
				builder.addAnimation("animation.feaster.death_ground", false);
			} else {
				builder.addAnimation("animation.feaster.idle_ground", true);
			}
		}
		animationController.setAnimation(builder);
		return PlayState.CONTINUE;
	}

	private <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
		final AnimationController animationController = event.getController();
		AnimationBuilder builder = new AnimationBuilder();
		if (this.dataTracker.get(IS_FLYING)) {
			if (this.dataTracker.get(IS_MELEE)) {
				builder.addAnimation("animation.feaster.attack_flight_melee", false);
			} else if (this.dataTracker.get(IS_RANGED)) {
				if (getCastTime() > 0) {
					builder.addAnimation("animation.feaster.attack_flight_ranged_lightening", false);
				} else {
					builder.addAnimation("animation.feaster.attack_flight_ranged_maina", false);
				}

			}
		}
		animationController.setAnimation(builder);
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	public int getCastTime() {
		return dataTracker.get(CASTING_TIME_LEFT);
	}

	@Override
	public void setCastTime(int castTime) {
		dataTracker.set(CASTING_TIME_LEFT, castTime);
	}

	@Override
	public boolean isCasting() {
		return dataTracker.get(CASTING_TIME_LEFT) > 0;
	}

	@Override
	public Spell getCurrentSpell() {
		return currentSpell;
	}

	@Override
	public void setCurrentSpell(Spell spell) {
		this.currentSpell = spell;
	}

	@Override
	public Spell selectSpell() {
		SpellMedium medium = random.nextBoolean() ? MMSpellMediums.BOLT : MMSpellMediums.PROJECTILE;
		SpellEffect effect = MMSpellEffects.DAMAGE;
		return new Spell(medium, effect, 2 + random.nextInt(3));
	}

	//AI

	private static class FeasterWanderGoal extends Goal {

		private final FeasterEntity feasterEntity;

		private FeasterWanderGoal(FeasterEntity feasterEntity) {
			this.feasterEntity = feasterEntity;
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		@Override
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

		@Override
		public boolean shouldContinue() {
			return !feasterEntity.getNavigation().isIdle();
		}

		@Override
		public void start() {
			Vec3d vec3d = this.getRandomLocation();
			if (vec3d != null) {
				feasterEntity.getNavigation().startMovingAlong(feasterEntity.getNavigation().findPathTo((new BlockPos(vec3d)), 2), 1.0D);
			}
		}

		@Nullable
		private Vec3d getRandomLocation() {
			return FuzzyTargeting.find(feasterEntity, 16, 16);
		}
	}

	private static class FeasterLookAtTargetGoal extends Goal {
		private final FeasterEntity feasterEntity;

		public FeasterLookAtTargetGoal(FeasterEntity feasterEntity) {
			this.feasterEntity = feasterEntity;
			this.setControls(EnumSet.of(Goal.Control.LOOK));
		}

		public boolean canStart() {
			return true;
		}

		public boolean shouldRunEveryTick() {
			return true;
		}

		public void tick() {
			if (this.feasterEntity.getTarget() == null) {
				Vec3d vec3d = this.feasterEntity.getVelocity();
				this.feasterEntity.setYaw(-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776F);
				this.feasterEntity.bodyYaw = this.feasterEntity.getYaw();
			} else {
				LivingEntity livingEntity = this.feasterEntity.getTarget();
				double d = 64.0D;
				if (livingEntity.squaredDistanceTo(this.feasterEntity) < 4096.0D) {
					double e = livingEntity.getX() - this.feasterEntity.getX();
					double f = livingEntity.getZ() - this.feasterEntity.getZ();
					this.feasterEntity.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776F);
					this.feasterEntity.bodyYaw = this.feasterEntity.getYaw();
				}
			}

		}
	}

	private static class FeasterManiaCloudAttackGoal extends Goal{
		private final FeasterEntity feasterEntity;
		public int cooldown;

		public FeasterManiaCloudAttackGoal(FeasterEntity feasterEntity) {
			this.feasterEntity = feasterEntity;
		}

		public boolean canStart() {
			return this.feasterEntity.getTarget() != null;
		}

		public void start() {
			this.cooldown = 0;
		}

		public boolean shouldRunEveryTick() {
			return true;
		}

		public void tick() {
			LivingEntity livingEntity = this.feasterEntity.getTarget();
			if (livingEntity != null) {
				double distance = 64D;
				if (livingEntity.squaredDistanceTo(this.feasterEntity) < distance * distance && this.feasterEntity.canSee(livingEntity)) {
					World world = this.feasterEntity.world;
					++this.cooldown;
					if (this.cooldown == 20 * 10) {
						var playerList = world.getNonSpectatingEntities(PlayerEntity.class, new Box(this.feasterEntity.getBlockPos()).expand(24)).stream().limit(3).toList();
						if(!playerList.isEmpty()){
							for (PlayerEntity player : playerList){
								player.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 20 * 30));
								if(!world.isClient()){
									ManiaCloudPacket.send(this.feasterEntity);
								}
							}
						}
						this.cooldown = - 20 * 10;
					}
				} else if (this.cooldown > 0) {
					--this.cooldown;
				}
			}
		}
	}

	private static class FeasterSwipeAttackGoal extends AttackGoal{
		private final FeasterEntity feasterEntity;
		private LivingEntity target;
		private int cooldown;
		public FeasterSwipeAttackGoal(FeasterEntity feasterEntity) {
			super(feasterEntity);
			this.feasterEntity = feasterEntity;
			this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		}
		@Override
		public boolean canStart() {
			LivingEntity livingEntity = this.feasterEntity.getTarget();
			if (livingEntity == null) {
				return false;
			} else {
				this.target = livingEntity;
				return true;
			}
		}
		@Override
		public boolean shouldContinue() {
			if (!this.target.isAlive()) {
				return false;
			} else if (this.feasterEntity.squaredDistanceTo(this.target) > 225.0D) {
				return false;
			} else {
				return !this.feasterEntity.getNavigation().isIdle() || this.canStart();
			}
		}
		@Override
		public void start() {
			super.start();
			this.feasterEntity.dataTracker.set(IS_MELEE, true);
		}


		@Override
		public void stop() {
			super.stop();
			this.feasterEntity.dataTracker.set(IS_MELEE, false);
		}

		@Override
		public void tick() {
			if(this.target != null){
				this.feasterEntity.getLookControl().lookAt(this.target, 30.0F, 30.0F);
				double d = (this.feasterEntity.getWidth() * 2.0F * this.feasterEntity.getWidth() * 2.0F);
				double e = this.feasterEntity.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
				double f = 0.8D;
				if (e > d && e < 16.0D) {
					f = 1.33D;
				} else if (e < 225.0D) {
					f = 0.6D;
				}

				this.feasterEntity.getNavigation().startMovingTo(this.target, f);
				this.cooldown = Math.max(this.cooldown - 1, 0);
				if (!(e > d)) {
					if (this.cooldown <= 0) {
						this.cooldown = 20;
						this.feasterEntity.trySwipeAttack(this.target);
					}
				}
			}
		}
	}
}
*/
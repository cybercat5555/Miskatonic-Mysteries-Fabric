package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.RenderTransformable;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.ai.CastSpellGoal;
import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;
import com.miskatonicmysteries.common.util.Util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class FeasterEntity extends HostileEntity implements IAnimatable, Affiliated, CastingMob {

	private static final byte NO_MOVE = 0;
	private static final byte ATTACK = 1;
	private static final byte GRABBING = 2;
	private static final byte GRABBED = 3;
	private static final byte CHANGE_FLIGHT = 4;
	private static final byte PREPARING_SPELL = 5;
	private static final byte SPELL_BOLT = 6;
	private static final byte SPELL_MANIA = 7;

	private static final TrackedData<Boolean> IS_FLYING = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final TrackedData<Byte> DISTINCT_MOVE_ID = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.BYTE);
	private static final TrackedData<Integer> DISTINCT_MOVE_TICKS = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> GRABBED_ENTITY_ID = DataTracker.registerData(FeasterEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private final MoveControl airMoveControl;
	private final MoveControl groundMoveControl;
	private final EntityNavigation airNavigation;
	private final EntityNavigation groundNavigation;
	@Nullable
	public Spell currentSpell;
	private AnimationFactory factory = new AnimationFactory(this);
	private LivingEntity grabbedEntity;


	public FeasterEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.experiencePoints = 50;
		this.airMoveControl = new FlightMoveControl(this, 12, true);
		this.groundMoveControl = new MoveControl(this);
		this.airNavigation = new BirdNavigation(this, world);
		this.groundNavigation = new MobNavigation(this, world);
		this.moveControl = groundMoveControl;
		this.navigation = groundNavigation;
	}

	public static DefaultAttributeContainer.Builder createAttributes() {
		return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 150)
			.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.32F)
			.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.4)
			.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(1, new SwitchMovementModeGoal());
		this.goalSelector.add(2, new DrainTargetGoal());
		this.goalSelector.add(3, new DragTargetIntoSkyGoal());
		this.goalSelector.add(4, new CastSpellGoal<>(this) {
			@Override
			public boolean canStart() {
				return getDistinctMoveTicks() == 0 && isFlying() && super.canStart() && distanceTo(getTarget()) > 6 && distanceToGround() < 16
					&& random.nextBoolean();
			}

			@Override
			public void stop() {
				super.stop();
				setDistinctMoveTicks(0);
			}
		});
		this.goalSelector.add(5, new MoveUpToTargetGoal());
		this.goalSelector.add(6, new FeasterSwipeGoal());
		this.goalSelector.add(7, new FeasterWanderGoal());
		this.targetSelector.add(1, new RevengeGoal(this, FeasterEntity.class, HasturCultistEntity.class, TatteredPrinceEntity.class));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, LivingEntity.class, 10, false, true, entity -> {
			if (entity instanceof RaiderEntity) {
				return true;
			}
			Affiliation affiliation = MiskatonicMysteriesAPI.getPracticallyApparentAffiliation(entity);
			if (affiliation == MMAffiliations.HASTUR) {
				return false;
			}
			return affiliation != MMAffiliations.NONE || entity instanceof PlayerEntity;
		}));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(IS_FLYING, true);
		this.dataTracker.startTracking(DISTINCT_MOVE_TICKS, 0);
		this.dataTracker.startTracking(DISTINCT_MOVE_ID, (byte) 0);
		this.dataTracker.startTracking(GRABBED_ENTITY_ID, -1);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean(NBT.IS_FLYING, dataTracker.get(IS_FLYING));
		nbt.putInt(NBT.TRANSITION_TICKS, getDistinctMoveTicks());
		nbt.putByte(NBT.SPECIAL_MOVE, getDistinctMoveId());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setFlying(nbt.getBoolean(NBT.IS_FLYING));
		setDistinctMoveTicks(nbt.getInt(NBT.TRANSITION_TICKS));
		setDistinctMoveId(nbt.getByte(NBT.SPECIAL_MOVE));
	}

	@Override
	protected void mobTick() {
		super.mobTick();
		if (!world.isClient) {
			byte distinctMove = getDistinctMoveId();
			if (distinctMove != GRABBED && grabbedEntity != null) {
				distinctMove = GRABBED;
				setDistinctMoveId(GRABBED);
			} else if (distinctMove == GRABBED && grabbedEntity == null) {
				setDistinctMoveId(NO_MOVE);
				setGrabbedEntity(null);
			}
			if (distinctMove <= CHANGE_FLIGHT && distinctMove > NO_MOVE) {
				int progress = getDistinctMoveTicks();
				if (progress > 0) {
					progress--;
					if (distinctMove == CHANGE_FLIGHT) {
						boolean flying = isFlying();
						if (!flying && progress <= 5) {
							addVelocity(0, 0.2, 0);
						}
						if (progress == 0) {
							setFlying(!flying);
						}
					}
					setDistinctMoveTicks(progress);
				}
			}
			if (getTarget() != null && distinctMove != GRABBED && distinctMove != GRABBING) {
				if (isCasting() && currentSpell != null) {
					EffectParticlePacket.send(this);
				}
			}

			if (grabbedEntity != null && grabbedEntity.isDead()) {
				setGrabbedEntity(null);
			}
		}
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		return super.interactMob(player, hand);
	}

	@Override
	public boolean tryAttack(Entity target) {
		if (super.tryAttack(target)) {
			if (target instanceof LivingEntity l) {
				switch (random.nextInt(4)) {
					case 0:
						l.addStatusEffect(new StatusEffectInstance(MMStatusEffects.BLEED, 200, 1, true, false, false));
						break;
					case 1:
						l.damage(new Constants.DamageSources.FeasterDamageSource(this), 2);
						break;
					default:
						break;
				}
			}
			return true;
		}
		return false;
	}

	public byte getDistinctMoveId() {
		return dataTracker.get(DISTINCT_MOVE_ID);
	}

	private void setDistinctMoveId(byte id) {
		dataTracker.set(DISTINCT_MOVE_ID, id);
	}

	public int getDistinctMoveTicks() {
		return dataTracker.get(DISTINCT_MOVE_TICKS);
	}

	public void setDistinctMoveTicks(int progress) {
		this.dataTracker.set(DISTINCT_MOVE_TICKS, progress);
		if (progress == 0) {
			setDistinctMoveId(NO_MOVE);
		}
	}

	public boolean isFlying() {
		return dataTracker.get(IS_FLYING);
	}

	public void setFlying(boolean flying) {
		if (getDistinctMoveTicks() > 0) {
			setDistinctMoveTicks(0);
		}
		dataTracker.set(IS_FLYING, flying);
		changeNavigation();
	}

	private int distanceToGround() {
		for (int i = 0; i < 24; i++) {
			if (world.isTopSolid(getBlockPos().add(0, -i, 0), FeasterEntity.this)) {
				return i;
			}
		}
		return 24;
	}

	@Override
	public boolean canBreatheInWater() {
		return true;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (super.damage(source, hasEntityGrabbed() ? Math.min(amount, 2) : amount)) {
			if (!world.isClient && random.nextFloat() < 0.2F + amount / 20F) {
				setGrabbedEntity(null);
			}
			return true;
		}
		return false;
	}

	private void setGrabbedEntity(LivingEntity entity) {
		this.grabbedEntity = entity;
		this.dataTracker.set(GRABBED_ENTITY_ID, grabbedEntity == null ? -1 : entity.getId());
		setDistinctMoveId(entity == null ? NO_MOVE : GRABBED);
	}

	@Override
	public void takeKnockback(double strength, double x, double z) {
		if (grabbedEntity != null) {
			return;
		}
		super.takeKnockback(strength, x, z);
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}

	@Override
	public boolean canFreeze() {
		return false;
	}

	public boolean hasEntityGrabbed() {
		return getDistinctMoveId() == GRABBED;
	}

	private boolean isSpecialMoveBusy() {
		return getDistinctMoveId() != NO_MOVE;
	}

	@Override
	public void tickMovement() {
		Vec3d vec3d = this.getVelocity();
		if (!world.isClient && isFlying()) {
			setNoGravity(true);
			int requiredDistance = hasEntityGrabbed() ? getDropDownHeight() : 3;
			float maxUpwardsSpeed = hasEntityGrabbed() ? 0.1F : 0.05F;
			if (distanceToGround() < requiredDistance && vec3d.y < maxUpwardsSpeed) {
				vec3d = vec3d.add(0, 0.025F, 0);
			} else if (distanceToGround() > requiredDistance + 2 && vec3d.y > -maxUpwardsSpeed) {
				vec3d = vec3d.add(0, -0.025F, 0);
			}
			this.setVelocity(vec3d);
		} else {
			setNoGravity(false);
		}
		super.tickMovement();

		if (getGrabbedEntityId() != -1) {
			if (grabbedEntity == null && world.getEntityById(getGrabbedEntityId()) instanceof LivingEntity l) {
				this.grabbedEntity = l;
			}
		} else {
			this.grabbedEntity = null;
		}

		if (grabbedEntity != null) {
			if (isFlying()) {
				this.fallDistance = 0;
				grabbedEntity.setPosition(Util.getYawRelativePos(getPos(), 2.5, getYaw() + 25, 55));
			} else {
				grabbedEntity.setPosition(Util.getYawRelativePos(getPos(), 2, getYaw(), 0));
				if (world.isClient && grabbedEntity instanceof RenderTransformable t && t.mm_getSquishTicks() <= 0) {
					t.mm_squish();
				}
			}
		}
	}

	private int getDropDownHeight() {
		return 24;
	}

	public int getGrabbedEntityId() {
		return dataTracker.get(GRABBED_ENTITY_ID);
	}

	@Override
	public void registerControllers(AnimationData animationData) {
		animationData.addAnimationController(new AnimationController<>(this, "main_controller", 4, this::animationPredicate));
	}

	private <T extends IAnimatable> PlayState animationPredicate(AnimationEvent<T> event) {
		final AnimationController animationController = event.getController();
		AnimationBuilder builder = new AnimationBuilder();
		byte specialId = getDistinctMoveId();
		boolean flying = isFlying();
		if (specialId != NO_MOVE && specialId != GRABBED && specialId != PREPARING_SPELL) {
			if (specialId == CHANGE_FLIGHT) {
				if (flying) {
					builder.addAnimation("animation.feaster.land", false);
				} else {
					builder.addAnimation("animation.feaster.takeoff", false);
				}
			} else if (specialId == SPELL_BOLT) {
				builder.addAnimation("animation.feaster.attack_flight_ranged_lightening", false);
			} else if (specialId == SPELL_MANIA) {
				builder.addAnimation("animation.feaster.attack_flight_ranged_mania", false);
			} else if (specialId == GRABBING) {
				if (flying) {
					builder.addAnimation("animation.feaster.grab_flight", false);
				} else {
					//need animation wawawwaw
				}
			} else if (specialId == ATTACK) {
				if (flying) {
					builder.addAnimation("animation.feaster.attack_flight_melee", true);
				} else {
					builder.addAnimation("animation.feaster.attack_ground_melee", true);
				}
			}
		} else if (animationController.getAnimationState() != AnimationState.Running ||
			(animationController.getCurrentAnimation() != null)) {
			if (flying) {
				if (specialId == GRABBED) {
					builder.addAnimation("animation.feaster.hold_flight", true);
				} else {
					builder.addAnimation("animation.feaster.idle_flight", true);
				}
			} else {
				if (specialId == GRABBED) {
					builder.addAnimation("animation.feaster.idle_ground", true);
				} else if (event.getLimbSwingAmount() > 0.4F) {
					builder.addAnimation("animation.feaster.walk_ground", true);
				} else {
					builder.addAnimation("animation.feaster.idle_ground", true);
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

	public boolean transitionsFlight() {
		int progress = getDistinctMoveTicks();
		return progress % 100 != 0;
	}

	private void changeNavigation() {
		if (isFlying()) {
			this.moveControl = airMoveControl;
			this.navigation = airNavigation;
		} else {
			this.moveControl = groundMoveControl;
			this.navigation = groundNavigation;
		}
	}

	private void changeFlightMode() {
		setDistinctMoveId(CHANGE_FLIGHT);
		setDistinctMoveTicks(31); //animation durations
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.HASTUR;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}

	@Override
	public int getCastTime() {
		return getDistinctMoveTicks();
	}

	@Override
	public void setCastTime(int castTime) {
		this.setDistinctMoveTicks(castTime);
		if (castTime == 18 && currentSpell != null) {
			setDistinctMoveId(currentSpell.medium == MMSpellMediums.BOLT ? SPELL_BOLT : SPELL_MANIA);
		}
	}

	@Override
	public boolean isCasting() {
		return getDistinctMoveId() > CHANGE_FLIGHT && getDistinctMoveTicks() > 0;
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
		boolean bolt = random.nextBoolean() || getTarget() != null && getTarget().distanceTo(getTarget()) > 4 || getTarget()
			.hasStatusEffect(MMStatusEffects.MANIA);
		SpellMedium medium = bolt ? MMSpellMediums.BOLT : MMSpellMediums.PROJECTILE;
		SpellEffect effect = bolt ? MMSpellEffects.DAMAGE : MMSpellEffects.MANIA_CLOUD;
		setDistinctMoveId(PREPARING_SPELL);
		return new Spell(medium, effect, 1);
	}


	private class FeasterWanderGoal extends WanderAroundFarGoal {

		private FeasterWanderGoal() {
			super(FeasterEntity.this, 1.0D);
			this.setControls(EnumSet.of(Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return getTarget() == null && !transitionsFlight() && super.canStart();
		}

		@Override
		public boolean shouldContinue() {
			return !transitionsFlight() && super.shouldContinue();
		}

		@Nullable
		@Override
		protected Vec3d getWanderTarget() {
			return isFlying() ? getFlightTarget() : super.getWanderTarget();
		}

		@Nullable
		protected Vec3d getFlightTarget() {
			Vec3d vec3d = this.mob.getRotationVec(0.0f);
			setPosition(getPos().add(0, -2, 0));
			Vec3d vec3d2 = AboveGroundTargeting.find(this.mob, 12, 5, vec3d.x, vec3d.z, 1.5707963705062866F, 12, 8);
			setPosition(getPos().add(0, 2, 0));
			if (vec3d2 != null) {
				return vec3d2.add(0, 3, 0);
			}
			return NoPenaltySolidTargeting.find(this.mob, 12, 4, -2, vec3d.x, vec3d.z, 1.5707963705062866);
		}
	}

	private class SwitchMovementModeGoal extends Goal {

		private int switchCooldown = 20;
		private int groundTicks = 0;

		@Override
		public boolean canStart() {
			if (isSpecialMoveBusy()) {
				return false;
			}
			if (isSubmergedInWater()) {
				return !isFlying();
			}
			if (isFlying()) {
				switchCooldown--;
				if (getTarget() != null) {
					if (distanceTo(getTarget()) < 2) {
						return switchCooldown-- <= 0;
					}
					return getHealth() < getMaxHealth() / 3;
				} else {
					if (distanceToGround() < 6) {
						return groundTicks++ >= 20;
					} else {
						groundTicks = 0;
					}
				}
			} else {
				if (getHealth() < getMaxHealth() / 2) {
					return false;
				}
				if (getTarget() != null && distanceTo(getTarget()) > 5 && world.isSkyVisible(getBlockPos())) {
					return switchCooldown-- == 0;
				}
			}
			return false;
		}

		@Override
		public void start() {
			super.start();
			switchCooldown = 30;
			groundTicks = 0;
			changeFlightMode();
		}
	}

	private class DragTargetIntoSkyGoal extends Goal {

		private int cooldown;

		public DragTargetIntoSkyGoal() {
			super();
			setControls(EnumSet.of(Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return cooldown-- <= 0 && getTarget() != null && isFlying() && canGrab(getTarget());
		}

		private boolean canGrab(LivingEntity target) {
			return target.getBoundingBox().intersects(getBoundingBox().expand(2, 2, 2).offset(0, -2, 0));
		}

		@Override
		public boolean shouldContinue() {
			return getTarget() != null && isFlying() && getDistinctMoveId() != NO_MOVE;
		}

		@Override
		public void start() {
			super.start();
			setDistinctMoveId(GRABBING);
			setDistinctMoveTicks(15);
		}

		@Override
		public void stop() {
			super.stop();
			setDistinctMoveTicks(0);
			setGrabbedEntity(null);
			cooldown = 60;
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			super.tick();
			byte distinctMove = getDistinctMoveId();
			int progress = getDistinctMoveTicks();
			if (distinctMove == GRABBING) {
				if (progress == 1) {
					if (getTarget().getBoundingBox().intersects(getBoundingBox().expand(1, 2, 1).offset(0, -1, 0))) {
						setGrabbedEntity(getTarget());
					} else {
						stop();
					}
				}
				setDistinctMoveTicks(progress - 1);
			} else if (distinctMove == GRABBED && distanceToGround() >= getDropDownHeight()) {
				stop();
			}
		}
	}

	private class DrainTargetGoal extends Goal {

		private int cooldown = 60;

		public DrainTargetGoal() {
			super();
			setControls(EnumSet.of(Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return cooldown-- <= 0 && getTarget() != null && !isFlying() && canGrab(getTarget());
		}

		private boolean canGrab(LivingEntity target) {
			return target.getBoundingBox()
				.intersects(getBoundingBox().expand(1, 2, 1).offset(Util.getYawRelativePosRelatively(2, getYaw(), getPitch())));
		}

		@Override
		public boolean shouldContinue() {
			return getTarget() != null && !isFlying() && getDistinctMoveId() != NO_MOVE;
		}

		@Override
		public void start() {
			super.start();
			setDistinctMoveId(GRABBING);
			setDistinctMoveTicks(15);
		}

		@Override
		public void stop() {
			super.stop();
			setDistinctMoveTicks(0);
			setGrabbedEntity(null);
			cooldown = 60;
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			super.tick();
			byte distinctMove = getDistinctMoveId();
			int progress = getDistinctMoveTicks();
			if (distinctMove == GRABBING) {
				if (progress == 1) {
					if (canGrab(getTarget())) {
						setGrabbedEntity(getTarget());
					} else {
						stop();
					}
				}
				setDistinctMoveTicks(progress - 1);
			} else if (distinctMove == GRABBED) {
				if (grabbedEntity != null) {
					if (age % 20 == 0) {
						playSound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK, 0.8F, 0.5F);
						grabbedEntity.damage(new Constants.DamageSources.FeasterDamageSource(FeasterEntity.this), 2);
						grabbedEntity.addStatusEffect(new StatusEffectInstance(MMStatusEffects.BRAIN_DRAIN, 1200, 0, true, true));
					}
				} else {
					stop();
				}
			}
		}
	}

	private class FeasterSwipeGoal extends Goal {

		public FeasterSwipeGoal() {
			super();
			setControls(EnumSet.of(Control.MOVE));
		}

		@Override
		public boolean canStart() {
			return getTarget() != null && canHit(getTarget());
		}

		private boolean canHit(LivingEntity target) {
			if (isFlying()) {
				return target.getBoundingBox()
					.intersects(getBoundingBox().expand(1, 1.5, 1)
									.offset(Util.getYawRelativePosRelatively(1, getYaw(), getPitch()))
									.offset(0, -2, 0));
			}
			return target.getBoundingBox()
				.intersects(getBoundingBox().expand(1, 2, 1).offset(Util.getYawRelativePosRelatively(2, getYaw(), getPitch())));
		}

		@Override
		public boolean shouldContinue() {
			LivingEntity livingEntity = getTarget();
			if (livingEntity == null) {
				return false;
			}
			return livingEntity.isAlive() && getDistinctMoveId() == ATTACK;
		}

		@Override
		public void start() {
			super.start();
			setDistinctMoveId(ATTACK);
			setDistinctMoveTicks(14);
		}

		@Override
		public void stop() {
			super.stop();
			setDistinctMoveTicks(0);
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			super.tick();
			byte currentMove = getDistinctMoveId();
			if (currentMove == ATTACK) {
				int progress = getDistinctMoveTicks();
				if (progress == 4) {
					List<Entity> entities = world
						.getOtherEntities(FeasterEntity.this, getBoundingBox().expand(5, 5, 5),
										  entity -> entity instanceof LivingEntity l && canHit(l));
					for (Entity entity : entities) {
						FeasterEntity.this.tryAttack(entity);
					}
				}
			}
		}
	}

	private class MoveUpToTargetGoal extends Goal {

		private final double speed = 1.0D;
		private Path path;
		private int updateCountdownTicks, cooldown;
		private double targetX, targetY, targetZ;

		public MoveUpToTargetGoal() {
			super();
			this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		}

		@Override
		public boolean canStart() {
			LivingEntity target = getTarget();
			if (getTarget() != null) {
				this.path = getNavigation().findPathTo(target, 32);
				return path != null;
			}
			return false;
		}

		@Override
		public boolean shouldContinue() {
			LivingEntity livingEntity = getTarget();
			if (livingEntity == null) {
				return false;
			}
			if (!livingEntity.isAlive()) {
				return false;
			}
			return isInWalkTargetRange(livingEntity.getBlockPos()) && distanceTo(getTarget()) > 3;
		}

		@Override
		public void start() {
			super.start();
			getNavigation().startMovingAlong(this.path, this.speed);
			this.updateCountdownTicks = 0;
			this.cooldown = 0;
		}

		@Override
		public void stop() {
			super.stop();
			getNavigation().stop();
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			super.tick();
			LivingEntity livingEntity = getTarget();
			if (livingEntity == null) {
				return;
			}
			getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
			double d = squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
			this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
			if (getVisibilityCache().canSee(livingEntity) && this.updateCountdownTicks <= 0 &&
				(this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 ||
					livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || getRandom().nextFloat() < 0.05f)) {
				this.targetX = livingEntity.getX();
				this.targetY = livingEntity.getY();
				this.targetZ = livingEntity.getZ();
				this.updateCountdownTicks = 4 + getRandom().nextInt(7);
				if (d > 1024.0) {
					this.updateCountdownTicks += 10;
				} else if (d > 256.0) {
					this.updateCountdownTicks += 5;
				}
				if (!getNavigation().startMovingTo(livingEntity, this.speed)) {
					this.updateCountdownTicks += 15;
				}
				this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
			}
			this.cooldown = Math.max(this.cooldown - 1, 0);
		}
	}
}

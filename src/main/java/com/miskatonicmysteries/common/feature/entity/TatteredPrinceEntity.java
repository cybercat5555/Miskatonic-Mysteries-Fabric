package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.ai.CastSpellGoal;
import com.miskatonicmysteries.common.feature.entity.brain.HasturCultistBrain;
import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.EffectParticlePacket;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.VisionPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LongDoorInteractGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class TatteredPrinceEntity extends PathAwareEntity implements IAnimatable, Affiliated, CastingMob {

	protected static final TrackedData<Integer> CASTING_TIME_LEFT = DataTracker
		.registerData(TatteredPrinceEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<Integer> BLESSING_TIME = DataTracker
		.registerData(TatteredPrinceEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private final ServerBossBar bossBar;
	private final AnimationFactory factory = new AnimationFactory(this);
	@Nullable
	public Spell currentSpell;
	public LivingEntity blessTarget;

	public TatteredPrinceEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
		bossBar = new ServerBossBar(getDisplayName(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS);
		((MobNavigation) this.getNavigation())
			.setCanPathThroughDoors(true);
		this.getNavigation().setCanSwim(true);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		if (damageSource == DamageSource.OUT_OF_WORLD) {
			return false;
		} else if (isInvulnerable()) {
			return true;
		} else {
			return damageSource != DamageSource.MAGIC && damageSource != DamageSource.GENERIC && damageSource.getAttacker() == null;
		}
	}

	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		bossBar.setName(getDisplayName());
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		bossBar.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		bossBar.removePlayer(player);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new LongDoorInteractGoal(this, false));
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(2, new BlessGoal());
		this.goalSelector.add(3, new CastSpellGoal<>(this));
		this.goalSelector.add(3, new ChokeTargetGoal());
		this.goalSelector.add(4, new SwingAtTargetGoal());
		this.goalSelector.add(5, new LookAroundGoal(this));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12));
		this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
		this.targetSelector.add(0, new RevengeGoal(this, TatteredPrinceEntity.class));
		super.initGoals();
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(CASTING_TIME_LEFT, 0);
		dataTracker.startTracking(BLESSING_TIME, 0);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			if (getBlessingTicks() > 0 && getBlessTarget() != null) {
				Vec3d pos = Util.getYawRelativePos(getPos(), 2.5, getYaw(), 0);
				world.addParticle(MMParticles.AMBIENT, pos.x + random.nextGaussian() * 1.5F, pos.y + 2 + random.nextFloat() * 1.5F,
								  pos.z + random.nextGaussian() * 1.5F, 1, random.nextFloat(), 0);
			}
		}
		super.tick();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putInt(Constants.NBT.CASTING, getCastTime());
		if (currentSpell != null) {
			NbtCompound spell = currentSpell.toTag(new NbtCompound());
			tag.put(Constants.NBT.SPELL, spell);
		}
		//don't save blessing stuff to tag
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		if (hasCustomName()) {
			bossBar.setName(getDisplayName());
		}
		if (tag.contains(Constants.NBT.SPELL)) {
			currentSpell = Spell.fromTag((NbtCompound) tag.get(Constants.NBT.SPELL));
		}
		setCastTime(tag.getInt(Constants.NBT.CASTING));
	}

	@Override
	public void tickMovement() {
		int maxProgress = 40;
		int hitTick = 20;
		if (this.handSwinging) {
			++this.handSwingTicks;
			if (handSwingTicks == hitTick && getTarget() != null) {
				tryAttack(getTarget());
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
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Override
	protected void mobTick() {
		super.mobTick();
		if (!isAttacking()) {
			heal(1);
		}
		if (isCasting()) {
			if (currentSpell != null && !world.isClient) {
				EffectParticlePacket.send(this);
				if (getTarget() == null) {
					setCastTime(0);
				}
			}
		}
		if (getBlessingTicks() > 0) {
			decreaseBlessingTicks();
		}
		bossBar.setPercent(getHealth() / getMaxHealth());
	}

	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
								 @Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (!player.world.isClient && getBlessingTicks() <= 0 && MiskatonicMysteriesAPI
			.canLevelUp(Ascendant.of(player).get(), Affiliated.of(player).get(), 2, getAffiliation(false))) {
			Vec3d pos = Util.getYawRelativePos(getPos().add(0, 2, 0), 2.5, getYaw(), 0);
			Vec3d motionVec = new Vec3d(pos.x - player.getX(), pos.y - player.getY(), pos.z - player.getZ());
			if (motionVec.length() < 4) {
				setBlessTarget(player);
				dataTracker.set(BLESSING_TIME, 0);
				return ActionResult.SUCCESS;
			}
		}
		return super.interactMob(player, hand);
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player) {
		return false;
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.HASTUR;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}

	public void decreaseBlessingTicks() {
		dataTracker.set(BLESSING_TIME, getBlessingTicks() - 1);
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
	@Nullable
	public Spell getCurrentSpell() {
		return currentSpell;
	}

	@Override
	public void setCurrentSpell(@Nullable Spell currentSpell) {
		this.currentSpell = currentSpell;
	}

	@Override
	public Spell selectSpell() {
		SpellMedium medium = random.nextBoolean() ? MMSpellMediums.BOLT : MMSpellMediums.PROJECTILE;
		SpellEffect effect = MMSpellEffects.DAMAGE;
		return new Spell(medium, effect, 2 + random.nextInt(3));
	}

	public int getBlessingTicks() {
		return dataTracker.get(BLESSING_TIME);
	}

	public @Nullable
	LivingEntity getBlessTarget() {
		return blessTarget;
	}

	public void setBlessTarget(LivingEntity blessTarget) {
		this.blessTarget = blessTarget;
	}

	@Override
	public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
		return world.getEntitiesByClass(TatteredPrinceEntity.class, getBoundingBox().expand(50, 50, 50), null).size() < 1;
	}

	@Override
	public void registerControllers(AnimationData data) {
		//separate controllers so the tentacles can always move independently
		data.addAnimationController(new AnimationController<>(this, "main_controller", 20, this::animationPredicate));
		data.addAnimationController(new AnimationController<>(this, "movement_controller", 20, this::movementAnimationPredicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	public <P extends IAnimatable> PlayState animationPredicate(AnimationEvent<P> event) {
		if (getBlessingTicks() > 100) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("bless", false));
			return PlayState.CONTINUE;
		}
		if (getHandSwingProgress(1) > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("attack_melee", false));
			return PlayState.CONTINUE;
		}
		if (getCastTime() > 0) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("attack_yeet_loop", true));
			return PlayState.CONTINUE;
		}
		if (event.isMoving()) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walking_upper", true));
			return PlayState.CONTINUE;
		}
		event.getController().setAnimation(new AnimationBuilder().addAnimation("body_idle", true));
		return PlayState.CONTINUE;
	}

	public <P extends IAnimatable> PlayState movementAnimationPredicate(AnimationEvent<P> event) {
		float limbSwingAmount = event.getLimbSwingAmount();
		boolean isMoving = !(limbSwingAmount > -0.4F && limbSwingAmount < 0.4F);
		if (isMoving) {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("walking_lower", true));
			return PlayState.CONTINUE;
		} else {
			event.getController().setAnimation(new AnimationBuilder().addAnimation("tentacle_idle", true));
		}
		return PlayState.CONTINUE;
	}

	public void startBlessing() {
		dataTracker.set(BLESSING_TIME, 204); //animation time + transition ticks + blessing vision part
	}

	@Override
	public boolean canTarget(LivingEntity target) {
		return (target instanceof PlayerEntity || MiskatonicMysteriesAPI.getNonNullAffiliation(target, true) != MMAffiliations.HASTUR) && super
			.canTarget(target);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (!world.isClient && source.getAttacker() instanceof LivingEntity l) {
			List<HasturCultistEntity> cultists = world.getEntitiesByClass(HasturCultistEntity.class, getBoundingBox().expand(32, 16, 32),
																		  cultist -> true);
			for (HasturCultistEntity cultist : cultists) {
				HasturCultistBrain.onAttacked(cultist, l);
			}
		}
		return super.damage(source, amount);
	}

	@Override
	public boolean isAffectedBySplashPotions() {
		return false;
	}

	public class ChokeTargetGoal extends Goal {

		private int internalCooldown;
		private int chokeTimer;

		public ChokeTargetGoal() {
			setControls(EnumSet.of(Control.TARGET));
		}

		@Override
		public boolean canStart() {
			if (internalCooldown > 0) {
				internalCooldown--;
			}
			return internalCooldown <= 0 && getTarget() != null && getTarget().distanceTo(TatteredPrinceEntity.this) < 10;
		}

		@Override
		public boolean shouldContinue() {
			return chokeTimer > 0 && getTarget() != null;
		}

		@Override
		public void start() {
			chokeTimer = 100;
		}

		@Override
		public void stop() {
			chokeTimer = 0;
			internalCooldown = 100;
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (getTarget() == null) {
				stop();
				return;
			}
			LivingEntity target = getTarget();
			getLookControl().lookAt(target, 40, 40);
			Vec3d pos = Util.getYawRelativePos(getPos(), 4.5, getYaw(), 0);
			Vec3d motionVec = new Vec3d(pos.x - target.getX(), pos.y + 3 - target.getY(), pos.z - target.getZ());
			motionVec = motionVec.normalize().multiply(target.isOnGround() ? 0.4 : 0.24);
			target.setVelocity(motionVec);
			target.velocityModified = true;
			target.velocityDirty = true;
			chokeTimer--;
			if (chokeTimer % 40 == 0) {
				target.damage(DamageSource.magic(target, TatteredPrinceEntity.this), 2);
			}
		}
	}

	public class BlessGoal extends Goal {

		public BlessGoal() {
			setControls(EnumSet.of(Control.MOVE, Control.LOOK));
		}

		@Override
		public boolean canStart() {
			return !isAttacking() && getBlessTarget() != null && getBlessingTicks() == 0;
		}

		@Override
		public boolean shouldContinue() {
			return getBlessTarget() != null && getBlessingTicks() > 0;
		}

		@Override
		public void start() {
			startBlessing();
		}

		@Override
		public void stop() {
			setBlessTarget(null);
		}

		@Override
		public boolean shouldRunEveryTick() {
			return true;
		}

		@Override
		public void tick() {
			if (getBlessTarget() == null || getBlessTarget().isDead()) {
				stop();
				return;
			}

			LivingEntity target = getBlessTarget();
			if (getBlessingTicks() == 100 && target instanceof ServerPlayerEntity) {
				VisionPacket.send((ServerPlayerEntity) target, new Identifier(Constants.MOD_ID, "hastur_bless"));
			}
			getLookControl().lookAt(target, 40, 40);
			Vec3d pos = Util.getYawRelativePos(getPos(), 2.5, getYaw(), 0);
			Vec3d motionVec = new Vec3d(pos.x - target.getX(), pos.y + 2 - target.getY(), pos.z - target.getZ());
			motionVec = motionVec.normalize().multiply(target.isOnGround() ? 0.12F : 0.084F);
			target.setVelocity(motionVec);
			target.velocityModified = true;
			target.velocityDirty = true;
			if (getBlessingTicks() <= 1) {
				HasturAscensionHandler.blessThroughPrince(getBlessTarget(), TatteredPrinceEntity.this);
			}
		}
	}

	public class SwingAtTargetGoal extends Goal {

		private int updateCountdownTicks;

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
			updateCountdownTicks = 0;
		}

		public void stop() {
			LivingEntity livingEntity = getTarget();
			if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
				setTarget(null);
			}
			setAttacking(false);
			getNavigation().stop();
		}

		public void tick() {
			LivingEntity target = getTarget();
			double d = squaredDistanceTo(target);
			this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
			if ((canSee(target)) && this.updateCountdownTicks <= 0 || squaredDistanceTo(target) >= 1.0D
				|| getRandom().nextFloat() < 0.05F) {
				this.updateCountdownTicks = 4 + getRandom().nextInt(7);
				if (d > 1024.0D) {
					this.updateCountdownTicks += 10;
				} else if (d > 256.0D) {
					this.updateCountdownTicks += 5;
				}

				if (!getNavigation().startMovingTo(target, 1)) {
					this.updateCountdownTicks += 15;
				}
			}

			getLookControl().lookAt(target, 30, 30);
			if (this.getSquaredMaxAttackDistance(target) >= squaredDistanceTo(target) && !handSwinging) {
				swingHand(Hand.MAIN_HAND);
			}
		}

		protected double getSquaredMaxAttackDistance(LivingEntity entity) {
			return (getWidth() * 2.0F * getWidth() * 2.0F + entity.getWidth());
		}
	}
}

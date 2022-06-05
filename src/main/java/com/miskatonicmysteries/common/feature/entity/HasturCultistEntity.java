package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.entity.brain.HasturCultistBrain;
import com.miskatonicmysteries.common.feature.entity.util.CastingMob;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.handler.ascension.HasturAscensionHandler;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMEntities;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMSpellEffects;
import com.miskatonicmysteries.common.registry.MMSpellMediums;
import com.miskatonicmysteries.common.registry.MMTrades;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.mixin.entity.LivingEntityAccessor;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillageGossipType;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class HasturCultistEntity extends VillagerEntity implements Angerable, Affiliated, CastingMob {

	protected static final TrackedData<Integer> VARIANT = DataTracker
		.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.INTEGER);
	protected static final TrackedData<Integer> CASTING_TIME_LEFT = DataTracker
		.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.INTEGER);
	//anger
	private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(80, 120);
	@Nullable
	public Spell currentSpell;
	private int angerTime;
	@Nullable
	private UUID targetUuid;

	public HasturCultistEntity(EntityType<HasturCultistEntity> type, World world) {
		super(type, world);
		((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
		this.getNavigation().setCanSwim(true);
	}

	public static ItemStack createYellowSignShield() {
		ItemStack stack = new ItemStack(Items.SHIELD);
		NbtCompound tag = stack.getOrCreateSubNbt(Constants.NBT.BLOCK_ENTITY_TAG);
		tag.putInt(Constants.NBT.BANNER_BASE, DyeColor.BLACK.getId());
		NbtList bannerpptag = new NbtList();
		NbtCompound yellowTag = new NbtCompound();
		yellowTag.putString(Constants.NBT.BANNER_PATTERN, Constants.MOD_ID + ":yellow_sign");
		yellowTag.putInt(Constants.NBT.BANNER_COLOR, DyeColor.YELLOW.getId());
		bannerpptag.add(yellowTag);
		tag.put(Constants.NBT.BANNER_PP_TAG, bannerpptag);
		return stack;
	}

	@Override
	protected void mobTick() {
		super.mobTick();
		this.world.getProfiler().push("hasturCultistBrain");
		this.getBrain().tick((ServerWorld) this.world, this);
		this.world.getProfiler().pop();
		HasturCultistBrain.tickActivities(this);
	}

	@Override
	public @Nullable
	LivingEntity getAttacking() {
		return !world.isClient && getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent() ? getBrain()
			.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get() : super.getAttacking();
	}

	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		Brain<VillagerEntity> brain = this.createBrainProfile().deserialize(dynamic);
		this.initBrain(brain);
		return brain;
	}

	@Override
	public void reinitializeBrain(ServerWorld world) {
		Brain<VillagerEntity> brain = this.getBrain();
		brain.stopAllTasks(world, this);
		this.brain = brain.copy();
		this.initBrain(this.getBrain());
	}

	private void initBrain(Brain<VillagerEntity> brain) {
		HasturCultistBrain.init(this, brain);
	}

	@Override
	protected Brain.Profile<VillagerEntity> createBrainProfile() {
		return HasturCultistBrain.createProfile();
	}

	@Override
	public VillagerEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
		return null;
	}

	@Override
	public VillagerData getVillagerData() {
		return super.getVillagerData().withProfession(VillagerProfession.NITWIT); //always the same profession
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (getAttacking() != null) {
			return ActionResult.FAIL;
		}
		if (getVariant() == 2 && player.getStackInHand(hand).isIn(Constants.Tags.HASTUR_CULTIST_OFFERINGS)) {
			if (HasturAscensionHandler.offerArtToCultist(player, hand, this)) {
				getGossip().startGossip(player.getUuid(), VillageGossipType.MAJOR_POSITIVE, 25);
			}
			return ActionResult.SUCCESS;
		}
		if (isLoyalTo(player)) {
			world.sendEntityStatus(this, (byte) 14);
		}
		return super.interactMob(player, hand);
	}

	@Override
	public void trade(TradeOffer offer) {
		if (!world.isClient && random.nextInt(10) < getReputation(getCustomer()) && MiskatonicMysteriesAPI
			.addKnowledge(MMAffiliations.HASTUR.getId().getPath(), getCustomer())) {
			world.spawnEntity(new ExperienceOrbEntity(world, getX(), getY(), getZ(), 5));
		}
		super.trade(offer);
	}

	@Override
	protected void fillRecipes() {
		if (isAscended()) {
			VillagerData villagerData = this.getVillagerData();
			Int2ObjectMap<TradeOffers.Factory[]> trades = MMTrades.YELLOW_SERF_TRADE;
			if (!trades.isEmpty()) {
				TradeOffers.Factory[] tradeFactories = trades.get(villagerData.getLevel());
				if (tradeFactories != null) {
					TradeOfferList tradeOfferList = this.getOffers();
					this.fillRecipesFromPool(tradeOfferList, tradeFactories, 2);
				}
			}
		}
	}

	@Override
	public boolean isReadyToBreed() {
		return false; //this is a celibate
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		if (getEquippedStack(slot).isEmpty()) {
			super.equipStack(slot, stack);
		}
	}

	@Override
	public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
		//no witches :)
	}

	@Override
	public void tickMovement() {
		this.tickHandSwing();
		super.tickMovement();
	}

	@Override
	public void tick() {
		if (age % 100 == 0) {
			heal(1);
		}
		super.tick();
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if (amount > 0.0F && ((LivingEntityAccessor) this).callBlockedByShield(source)) {
			this.damageShield(amount);
			if (!source.isProjectile()) {
				Entity entity = source.getSource();
				if (entity instanceof LivingEntity) {
					this.takeShieldHit((LivingEntity) entity);
					world.sendEntityStatus(entity, (byte) 29);
				}
			}
		}
		boolean damage = super.damage(source, amount);
		if (this.world.isClient) {
			return false;
		} else if (damage && source.getAttacker() instanceof LivingEntity) {
			HasturCultistBrain.onAttacked(this, (LivingEntity) source.getAttacker());
			return true;
		}
		return damage;
	}

	@Override //they are stronk
	protected void knockback(LivingEntity target) {
		target.takeKnockback(target instanceof PlayerEntity ? 1 : 0.5F, target.getX() - this.getX(), target.getZ() - this.getZ());
	}

	@Override
	public void takeKnockback(double strength, double x, double z) {
		if (isBlocking()) {
			strength -= 0.3F;
		}
		super.takeKnockback(strength, x, z);
	}

	@Override
	protected void initEquipment(LocalDifficulty difficulty) {
		super.initEquipment(difficulty);

		this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(world.random.nextBoolean() ? Items.IRON_SWORD : MMObjects.ORNATE_DAGGER));
		if (!isAscended()) {
			this.equipStack(EquipmentSlot.OFFHAND, createYellowSignShield());
		}
	}

	@Override
	public void onDeath(DamageSource source) {
		super.onDeath(source);
		if (isAscended() && world instanceof ServerWorld) {
			int amount = 1 + random.nextInt(3);
			for (int i = 0; i < amount; i++) {
				TentacleEntity tentacle = MMEntities.GENERIC_TENTACLE.create(world);
				if (getAttacker() != null) {
					tentacle.setSpecificTarget(getAttacker());
				}
				tentacle.refreshPositionAndAngles(getX() + random.nextGaussian(), getY(), getZ() + random.nextGaussian(), 0.0F, 0.0F);
				tentacle.setHeadYaw(random.nextInt(360));
				tentacle.initialize((ServerWorld) world, world.getLocalDifficulty(getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
				((ServerWorld) world).spawnEntityAndPassengers(tentacle);
			}
		}
	}

	@Nullable
	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
		@Nullable EntityData entityData, @Nullable NbtCompound entityTag) {
		entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if ((spawnReason == SpawnReason.STRUCTURE && getVariant() != 2) || spawnReason == SpawnReason.CONVERSION) {
			dataTracker.set(VARIANT, random.nextInt(2));
		} else if (spawnReason != SpawnReason.EVENT && random.nextFloat() < 0.25F) {
			dataTracker.set(VARIANT, 2);
		}
		setLeftHanded(random.nextBoolean()); //more left-handedness
		initEquipment(difficulty);
		setVillagerData(getVillagerData().withProfession(VillagerProfession.NITWIT));
		if (this.world instanceof ServerWorld) {
			this.reinitializeBrain((ServerWorld) this.world);
		}
		return entityData;
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(VARIANT, 0);
		dataTracker.startTracking(CASTING_TIME_LEFT, 0);
	}

	public int getVariant() {
		return dataTracker.get(VARIANT);
	}

	public boolean isAscended() {
		return dataTracker.get(VARIANT) == 2;
	}

	public void ascend() {
		dataTracker.set(VARIANT, 2);
		fillRecipes();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound tag) {
		super.writeCustomDataToNbt(tag);
		tag.putInt(Constants.NBT.VARIANT, getVariant());
		tag.putInt(Constants.NBT.CASTING, getCastTime());
		if (currentSpell != null) {
			NbtCompound spell = currentSpell.toTag(new NbtCompound());
			tag.put(Constants.NBT.SPELL, spell);
		}
		if (world instanceof ServerWorld) {
			writeAngerToNbt(tag);
		}
	}


	@Override
	public void readCustomDataFromNbt(NbtCompound tag) {
		super.readCustomDataFromNbt(tag);
		dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
		if (tag.contains(Constants.NBT.SPELL)) {
			currentSpell = Spell.fromTag((NbtCompound) tag.get(Constants.NBT.SPELL));
		}
		setCastTime(tag.getInt(Constants.NBT.CASTING));
		if (this.world instanceof ServerWorld) {
			readAngerFromNbt(world, tag);
			this.reinitializeBrain((ServerWorld) this.world);
		}
	}

	@Override
	public int getAngerTime() {
		return angerTime;
	}

	@Override
	public void setAngerTime(int ticks) {
		this.angerTime = ticks;
	}

	@Override
	public @Nullable
	UUID getAngryAt() {
		return targetUuid;
	}

	@Override
	public void setAngryAt(@Nullable UUID uuid) {
		this.targetUuid = uuid;
	}

	@Override
	public void chooseRandomAngerTime() {
		setAngerTime(ANGER_TIME_RANGE.get(this.random));
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
		SpellEffect effect = MMSpellEffects.KNOCKBACK;
		SpellMedium medium = MMSpellMediums.MOB_TARGET;
		LivingEntity target = getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).get();
		int intensity = 1 + getRandom().nextInt(2);
		if (getRandom().nextBoolean() && getHealth() < getMaxHealth()) {
			effect = MMSpellEffects.HEAL;
			medium = MMSpellMediums.GROUP;
		} else if (getRandom().nextBoolean() && target.distanceTo(this) > 6) {
			effect = MMSpellEffects.DAMAGE;
			medium = MMSpellMediums.BOLT;
			intensity = 1;
		} else if (!hasStatusEffect(StatusEffects.RESISTANCE)) {
			effect = MMSpellEffects.RESISTANCE;
			medium = MMSpellMediums.GROUP;
		}
		return new Spell(medium, effect, intensity);
	}

	@Override
	protected Text getDefaultName() {
		return isAscended() ? new TranslatableText("entity.miskatonicmysteries.hastur_cultist_ascended")
			: new TranslatableText("entity.miskatonicmysteries.hastur_cultist");
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.HASTUR;
	}

	@Override
	public boolean isSupernatural() {
		return isAscended();
	}

	public boolean isLoyalTo(PlayerEntity player) {
		return getReputation(player) >= 20 || MiskatonicMysteriesAPI.hasKnowledge(MMAffiliations.HASTUR.getId().getPath(), player);
	}
}

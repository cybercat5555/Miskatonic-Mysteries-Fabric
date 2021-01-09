package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.entity.ai.FarRangeMeleeAttackGoal;
import com.miskatonicmysteries.common.entity.ai.SpellCastGoal;
import com.miskatonicmysteries.common.entity.ai.TacticalDrawbackGoal;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.spell.Spell;
import com.miskatonicmysteries.common.item.books.MMBookItem;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModEntities;
import com.miskatonicmysteries.common.lib.ModObjects;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import com.miskatonicmysteries.mixin.LivingEntityAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.Durations;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.UniversalAngerGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.IntRange;
import net.minecraft.village.VillagerData;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class HasturCultistEntity extends VillagerEntity implements Angerable, Affiliated {
    protected static final TrackedData<Integer> VARIANT = DataTracker.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> CASTING_TIME_LEFT = DataTracker.registerData(HasturCultistEntity.class, TrackedDataHandlerRegistry.INTEGER);

    //anger
    private static final IntRange ANGER_TIME_RANGE = Durations.betweenSeconds(80, 120);
    private int angerTime;
    @Nullable
    private UUID targetUuid;

    @Nullable
    public Spell currentSpell;

    public HasturCultistEntity(EntityType<HasturCultistEntity> type, World world) {
        super(type, world);
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.getNavigation().setCanSwim(true);
    }


    @Override
    public VillagerEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return null;
    }

    @Override
    public VillagerData getVillagerData() {
        return super.getVillagerData().withProfession(ModEntities.YELLOW_SERF); //always the same profession
    }

    @Override
    public void setVillagerData(VillagerData villagerData) {
        if (villagerData.getProfession() != ModEntities.YELLOW_SERF)
            villagerData.withProfession(ModEntities.YELLOW_SERF);
        super.setVillagerData(villagerData);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (getTarget() != null) {
            return ActionResult.FAIL;
        }
        if (player.getStackInHand(hand).getItem().equals(ModObjects.NECRONOMICON) && getReputation(player) >= 100 && !MMBookItem.hasKnowledge(Affiliation.HASTUR.getId().getPath(), player.getStackInHand(hand))) {
            MMBookItem.addKnowledge(Affiliation.HASTUR.getId().getPath(), player.getStackInHand(hand));
            if (!this.world.isClient()) {
                this.playSound(SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
                world.spawnEntity(new ExperienceOrbEntity(world, getX(), getY(), getZ(), 5));
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    protected void fillRecipes() {
        if (isAscended()) {
            super.fillRecipes();
        }
    }

    @Override
    public boolean isReadyToBreed() {
        return false; //this is a celibate
    }

    @Override
    protected Text getDefaultName() {
        return new TranslatableText(this.getType().getTranslationKey());
    }

    @Override
    protected void initGoals() { //also need them to not have some brain stuff
        this.goalSelector.add(3, new TacticalDrawbackGoal<>(this));
        this.goalSelector.add(4, new SpellCastGoal<>(this));
        this.goalSelector.add(5, new FarRangeMeleeAttackGoal(this, 0.6F, false));
        this.targetSelector.add(0, new RevengeGoal(this, HasturCultistEntity.class).setGroupRevenge());
        this.targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, true, living -> CapabilityUtil.getAffiliation(living, true) == Affiliation.SHUB));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, HostileEntity.class, 5, true, true, mob -> !(mob instanceof HasturCultistEntity) && !(mob instanceof CreeperEntity)));
        this.targetSelector.add(3, new UniversalAngerGoal<>(this, true));
        this.targetSelector.add(4, new FollowTargetGoal<>(this, PlayerEntity.class, 50, true, true, player -> {
            if (player instanceof PlayerEntity) {
                return getReputation((PlayerEntity) player) <= -100; //you are the bad guy
            }
            return false;
        }));

    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        if (getEquippedStack(slot).isEmpty()) super.equipStack(slot, stack);
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
        if (age % 100 == 0) heal(1);
        super.tick();
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (isCasting()) {
            setCastTime(getCastTime() - 1);
        }
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
        return super.damage(source, amount);
    }

    @Override
    public void swingHand(Hand hand) {
        this.swingHand(hand, true);
    }

    @Override //they are stronk
    protected void knockback(LivingEntity target) {
        target.takeKnockback(target instanceof PlayerEntity ? 1 : 0.5F, target.getX() - this.getX(), target.getZ() - this.getZ());
    }

    @Override
    public void takeKnockback(float strength, double x, double z) {
        if (isBlocking()) {
            strength -= 0.3F;
        }
        super.takeKnockback(strength, x, z);
    }

    @Override
    public boolean isBlocking() {
        return super.isBlocking();
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);

        this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(world.random.nextBoolean() ? Items.IRON_SWORD : ModObjects.ORNATE_DAGGER));
        if (!isAscended()) {
            this.equipStack(EquipmentSlot.OFFHAND, createYellowSignShield());
        }
    }

    public static ItemStack createYellowSignShield() {
        ItemStack stack = new ItemStack(Items.SHIELD);
        CompoundTag tag = stack.getOrCreateSubTag(Constants.NBT.BLOCK_ENTITY_TAG);
        tag.putInt(Constants.NBT.BANNER_BASE, DyeColor.BLACK.getId());
        ListTag bannerpptag = new ListTag();
        CompoundTag yellowTag = new CompoundTag();
        yellowTag.putString(Constants.NBT.BANNER_PATTERN, Constants.MOD_ID + ":yellow_sign");
        yellowTag.putInt(Constants.NBT.BANNER_COLOR, DyeColor.YELLOW.getId());
        bannerpptag.add(yellowTag);
        tag.put(Constants.NBT.BANNER_PP_TAG, bannerpptag);
        return stack;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
        dataTracker.set(VARIANT, random.nextInt(2));
        if (spawnReason != SpawnReason.EVENT && spawnReason != SpawnReason.STRUCTURE && random.nextFloat() < 0.25F) {
            dataTracker.set(VARIANT, 2);
        }
        setLeftHanded(random.nextBoolean()); //more left-handedness
        initEquipment(difficulty);
        setVillagerData(getVillagerData().withProfession(ModEntities.YELLOW_SERF));
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
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.VARIANT, getVariant());
        tag.putInt(Constants.NBT.CASTING, getCastTime());
        if (currentSpell != null) {
            CompoundTag spell = currentSpell.toTag(new CompoundTag());
            tag.put(Constants.NBT.SPELL, spell);
        }
        angerToTag(tag);
    }


    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
        if (tag.contains(Constants.NBT.SPELL)) {
            currentSpell = Spell.fromTag((CompoundTag) tag.get(Constants.NBT.SPELL));
        }
        setCastTime(tag.getInt(Constants.NBT.CASTING));
        angerFromTag((ServerWorld) world, tag);
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
        setAngerTime(ANGER_TIME_RANGE.choose(this.random));
    }

    public void setCastTime(int castTime) {
        dataTracker.set(CASTING_TIME_LEFT, castTime);
    }

    public int getCastTime() {
        return dataTracker.get(CASTING_TIME_LEFT);
    }

    public boolean isCasting() {
        return dataTracker.get(CASTING_TIME_LEFT) > 0;
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return Affiliation.HASTUR;
    }

    @Override
    public boolean isSupernatural() {
        return isAscended();
    }
}

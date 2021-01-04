package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.entity.ai.GunAttackGoal;
import com.miskatonicmysteries.common.entity.ai.MobBowAttackGoal;
import com.miskatonicmysteries.common.entity.ai.MobCrossbowAttackGoal;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.item.GunItem;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.ModObjects;
import com.miskatonicmysteries.common.lib.ModParticles;
import com.miskatonicmysteries.common.lib.util.CapabilityUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

import static com.miskatonicmysteries.common.lib.Constants.NBT.ALTERNATE_WEAPON;

public class ProtagonistEntity extends PathAwareEntity implements RangedAttackMob, CrossbowUser {
    protected static final Map<AbstractMap.SimpleEntry<EquipmentSlot, ItemStack>, Integer> ARMOR_MAP = new HashMap<>();
    protected static final Map<ItemStack, Integer> WEAPON_MAP = new HashMap<>();
    protected static final Map<ItemStack, Integer> ALT_WEAPON_MAP = new HashMap<>();
    protected static final Map<Integer, EntityAttributeModifier> MODIFIER_MAP = new HashMap<>();
    private static final EntityAttributeModifier DEFAULT_MOD = new EntityAttributeModifier("210caf3b-c286-4142-98d1-136e8b59b1b1", 0, EntityAttributeModifier.Operation.ADDITION);

    protected static final TrackedData<Boolean> LOADING = DataTracker.registerData(ProtagonistEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> VARIANT = DataTracker.registerData(ProtagonistEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> STAGE = DataTracker.registerData(ProtagonistEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Optional<UUID>> TARGET_UUID = DataTracker.registerData(ProtagonistEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public ItemStack alternateWeapon = ItemStack.EMPTY;

    public ProtagonistEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints = 0;
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.getNavigation().setCanSwim(true);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, 0);
        dataTracker.startTracking(STAGE, 0);
        dataTracker.startTracking(LOADING, false);
        dataTracker.startTracking(TARGET_UUID, Optional.empty());
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new LongDoorInteractGoal(this, false));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new SwitchWeaponsGoal(this));
        this.goalSelector.add(3, new GunAttackGoal(this));
        this.goalSelector.add(3, new MobBowAttackGoal<>(this, 1.2F, 20, 24));
        this.goalSelector.add(3, new MobCrossbowAttackGoal<>(this, 1.4F, 8F));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 12));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(0, new RevengeGoal(this, ProtagonistEntity.class));
        this.targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, true, CapabilityUtil::isAffiliated));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, true, player -> (getTargetUUID().isPresent() && player.getUuid().equals(getTargetUUID().get())) || (player instanceof ISanity && ((ISanity) player).getSanity() <= MiskatonicMysteries.config.protagonistAggressionThreshold)));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, HostileEntity.class, 5, true, true, mob -> !(mob instanceof ProtagonistEntity) && !(mob instanceof CreeperEntity)));
        super.initGoals();
    }

    public void removeAfterTargetKill() {
        for (int i = 0; i < 10; i++)
            world.addParticle(ModParticles.FLAME, getX() + random.nextGaussian() * getDimensions(EntityPose.STANDING).width, getY() + random.nextFloat() * getDimensions(EntityPose.STANDING).height, getZ() + random.nextGaussian() * getDimensions(EntityPose.STANDING).width, 1, 0, 0);
        for (int i = 0; i < 15; i++)
            world.addParticle(ParticleTypes.LARGE_SMOKE, getX() + random.nextGaussian() * getDimensions(EntityPose.STANDING).width, getY() + random.nextFloat() * getDimensions(EntityPose.STANDING).height, getZ() + random.nextGaussian() * getDimensions(EntityPose.STANDING).width, 0, 0, 0);
        ProtagonistHandler.removeProtagonist(world, this);
        remove();
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        setCanPickUpLoot(true);
        if (spawnReason != SpawnReason.EVENT) {
            dataTracker.set(VARIANT, random.nextInt(4));
            dataTracker.set(STAGE, random.nextInt(4));
        }
        initEquipment(difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected void updatePostDeath() {
        if (getTargetUUID().isPresent() && getStage() < Constants.DataTrackers.PROTAGONIST_MAX_LEVEL) {
            if (getAttacker() instanceof PlayerEntity || (getAttacker() instanceof TameableEntity && getTargetUUID().isPresent() && getTargetUUID().get().equals(((TameableEntity) getAttacker()).getOwnerUuid()))) {
                if (!world.isClient)
                    ProtagonistHandler.levelProtagonist(world, this);
            }
            if (!world.isClient) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeDouble(getX());
                data.writeDouble(getY());
                data.writeDouble(getZ());
                PacketHandler.sendToPlayers(world, data, PacketHandler.PROTAG_PARTICLE_PACKET);
            }
            remove();
        } else {
            if (!world.isClient)
                ProtagonistHandler.removeProtagonist(world, this);
            ++this.deathTime;
            if (this.deathTime == 40) {
                this.remove();

                for (int i = 0; i < 20; ++i) {
                    double d = this.random.nextGaussian() * 0.02D;
                    double e = this.random.nextGaussian() * 0.02D;
                    double f = this.random.nextGaussian() * 0.02D;
                    this.world.addParticle(ParticleTypes.POOF, this.getParticleX(1.0D), this.getRandomBodyY(), this.getParticleZ(1.0D), d, e, f);
                }
            }
        }
    }

    @Override
    protected boolean prefersNewEquipment(ItemStack newStack, ItemStack oldStack) {
        return oldStack.isEmpty() && (isValidRangedItem(newStack.getItem()) || newStack.getItem() instanceof SwordItem);
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {

    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);
        ARMOR_MAP.keySet().stream().filter(e -> getStage() >= ARMOR_MAP.get(e)).sorted(Comparator.comparingInt(e -> ARMOR_MAP.get(e)).reversed()).forEachOrdered(entry -> {
            if (!hasStackEquipped(entry.getKey()) && random.nextFloat() < 0.75F)
                equipStack(entry.getKey(), entry.getValue());
        });
        WEAPON_MAP.keySet().stream().filter(stack -> getStage() >= WEAPON_MAP.get(stack)).sorted(Comparator.comparingInt(stack -> WEAPON_MAP.get(stack))).forEachOrdered(stack -> {
            if (getStackInHand(Hand.MAIN_HAND).isEmpty() && random.nextFloat() < 0.7F)
                setStackInHand(Hand.MAIN_HAND, stack);
        });
        if (getStackInHand(Hand.MAIN_HAND).isEmpty()) setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));

        ALT_WEAPON_MAP.keySet().stream().filter(stack -> getStage() >= ALT_WEAPON_MAP.get(stack)).sorted(Comparator.comparingInt(stack -> ALT_WEAPON_MAP.get(stack))).forEachOrdered(stack -> {
            if (alternateWeapon.isEmpty() && random.nextFloat() < 0.6F) alternateWeapon = stack;
        });
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        super.equipStack(slot, stack);
    }

    @Override
    public void tickMovement() {
        this.tickHandSwing();
        super.tickMovement();
    }

    @Override
    public void remove() {
        if (!world.isClient && getTargetUUID().isPresent())
            ProtagonistHandler.setSpawnState(this, false);
        super.remove();
    }

    public int getVariant() {
        return dataTracker.get(VARIANT);
    }

    public int getStage() {
        return dataTracker.get(STAGE);
    }

    public void setStage(int stage) {
        dataTracker.set(STAGE, stage);
    }

    @Nullable
    public PlayerEntity getTargetPlayer() {
        return getTargetUUID().isPresent() ? world.getPlayerByUuid(getTargetUUID().get()) : null;
    }

    public void setTargetUUID(UUID targetUUID) {
        dataTracker.set(TARGET_UUID, Optional.of(targetUUID));
    }

    public Optional<UUID> getTargetUUID() {
        return dataTracker.get(TARGET_UUID);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.VARIANT, getVariant());
        tag.putInt(Constants.NBT.STAGE, getStage());
        CompoundTag alternateWeaponTag = new CompoundTag();
        alternateWeapon.toTag(alternateWeaponTag);
        tag.put(ALTERNATE_WEAPON, alternateWeaponTag);
        tag.putBoolean(Constants.NBT.CHARGING, dataTracker.get(LOADING));
        if (getTargetUUID().isPresent())
            tag.putUuid(Constants.NBT.PLAYER_UUID, getTargetUUID().get());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
        setStage(tag.getInt(Constants.NBT.STAGE));
        alternateWeapon = ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.ALTERNATE_WEAPON));
        setCharging(tag.getBoolean(Constants.NBT.CHARGING));
        if (tag.contains(Constants.NBT.PLAYER_UUID))
            setTargetUUID(tag.getUuid(Constants.NBT.PLAYER_UUID));
    }

    public void switchWeapons() {
        if (!alternateWeapon.isEmpty()) {
            ItemStack stack = getMainHandStack();
            setStackInHand(Hand.MAIN_HAND, alternateWeapon);
            alternateWeapon = stack;
        }
    }

    private boolean isValidRangedItem(Item item) {
        return item instanceof GunItem || item instanceof RangedWeaponItem;
    }

    @Override
    public void attack(LivingEntity target, float pullProgress) {
        ItemStack itemStack = getArrowType(getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
        PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(this, itemStack, pullProgress);
        double d = target.getX() - getX();
        double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
        double f = target.getZ() - getZ();
        double g = MathHelper.sqrt(d * d + f * f);
        persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float) (14 - world.getDifficulty().getId() * 4));
        playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
        world.spawnEntity(persistentProjectileEntity);
    }

    @Override
    public void setCharging(boolean charging) {
        dataTracker.set(LOADING, charging);
    }

    public boolean isCharging() {
        return dataTracker.get(LOADING);
    }

    @Override
    public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
        this.shoot(this, target, projectile, multiShotSpray, 1.6F);
    }

    @Override
    public void postShoot() {

    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    static {
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE)), 0);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS)), 0);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE)), 1);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS)), 1);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE)), 2);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS)), 2);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE)), 3);
        ARMOR_MAP.put(new AbstractMap.SimpleEntry<>(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS)), 3);

        WEAPON_MAP.put(new ItemStack(Items.IRON_SWORD), 0);
        WEAPON_MAP.put(new ItemStack(Items.IRON_AXE), 0);
        WEAPON_MAP.put(new ItemStack(Items.DIAMOND_SWORD), 2);
        WEAPON_MAP.put(new ItemStack(Items.DIAMOND_AXE), 2);
        WEAPON_MAP.put(new ItemStack(Items.NETHERITE_SWORD), 3);
        WEAPON_MAP.put(new ItemStack(Items.NETHERITE_AXE), 3);

        ALT_WEAPON_MAP.put(new ItemStack(Items.BOW), 0);
        ALT_WEAPON_MAP.put(new ItemStack(Items.CROSSBOW), 1);
        ALT_WEAPON_MAP.put(new ItemStack(ModObjects.REVOLVER), 2);
        ALT_WEAPON_MAP.put(new ItemStack(ModObjects.RIFLE), 3);

        MODIFIER_MAP.put(0, DEFAULT_MOD);
        MODIFIER_MAP.put(1, new EntityAttributeModifier("8dd16ced-6e54-4f36-85a6-2fa9db05f08a", 5, EntityAttributeModifier.Operation.ADDITION));
        MODIFIER_MAP.put(2, new EntityAttributeModifier("abddf77b-7875-42cb-afd1-a90dd15c6174", 10, EntityAttributeModifier.Operation.ADDITION));
        MODIFIER_MAP.put(3, new EntityAttributeModifier("4b41f63d-4a31-48be-ac60-cea6e8a4e955", 15, EntityAttributeModifier.Operation.ADDITION));

    }

    public void setData(ProtagonistData data) {
        setStage(data.level);
        dataTracker.set(VARIANT, data.skin);
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(MODIFIER_MAP.getOrDefault(data.level, DEFAULT_MOD));
    }

    public static class SwitchWeaponsGoal extends Goal {
        private final ProtagonistEntity protagonist;

        public SwitchWeaponsGoal(ProtagonistEntity protagonist) {
            this.protagonist = protagonist;
        }

        @Override
        public boolean canStart() {
            return protagonist.getTarget() != null && shouldSwitchWeapon();
        }

        @Override
        public boolean shouldContinue() {
            return canStart();
        }

        @Override
        public void start() {
            super.start();
            protagonist.switchWeapons();
        }

        private boolean shouldSwitchWeapon() {
            if (protagonist.getMainHandStack().isEmpty()) return true;

            if (protagonist.isValidRangedItem(protagonist.getMainHandStack().getItem())) {
                return protagonist.alternateWeapon.getItem() instanceof SwordItem && protagonist.distanceTo(protagonist.getTarget()) < 4;
            }
            return protagonist.distanceTo(protagonist.getTarget()) > 4 && protagonist.isValidRangedItem(protagonist.alternateWeapon.getItem());
        }
    }

    public static class ProtagonistData {
        public int level, skin;
        public boolean spawned;

        public ProtagonistData(int level, int skin, boolean spawned) {
            this.level = level;
            this.skin = skin;
            this.spawned = spawned;
        }

        public void toTag(CompoundTag compoundTag) {
            compoundTag.putInt(Constants.NBT.STAGE, level);
            compoundTag.putInt(Constants.NBT.VARIANT, skin);
            compoundTag.putBoolean(Constants.NBT.SPAWNED, spawned);
        }

        public static ProtagonistData fromTag(CompoundTag compoundTag) {
            return new ProtagonistData(compoundTag.getInt(Constants.NBT.STAGE), compoundTag.getInt(Constants.NBT.VARIANT), compoundTag.getBoolean(Constants.NBT.SPAWNED));
        }
    }
}

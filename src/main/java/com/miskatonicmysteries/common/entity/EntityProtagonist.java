package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.IntRange;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.miskatonicmysteries.lib.util.Constants.DataTrackers.STAGE;
import static com.miskatonicmysteries.lib.util.Constants.DataTrackers.VARIANT;
import static com.miskatonicmysteries.lib.util.Constants.NBT.ALTERNATE_WEAPON;

public class EntityProtagonist extends HostileEntity implements RangedAttackMob, CrossbowUser {
    private static final TrackedData<Boolean> LOADING = DataTracker.registerData(EntityProtagonist.class, TrackedDataHandlerRegistry.BOOLEAN);
    public ItemStack alternateWeapon = ItemStack.EMPTY;

    public EntityProtagonist(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(VARIANT, 0);
        dataTracker.startTracking(STAGE, 0);
        dataTracker.startTracking(LOADING, false);
        super.initDataTracker();
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        //ai to switch to more convenient weapon if needed
        this.goalSelector.add(1, new SwitchWeaponsGoal(this));
        this.goalSelector.add(2, new ProtagonistBowAttackGoal(this, 1.2F, 20, 24));
        this.goalSelector.add(2, new CrossbowAttackGoal<>(this, 1.4F, 8F));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 12));
        this.goalSelector.add(6, new WanderAroundGoal(this, 1.0D));
        this.targetSelector.add(0, new RevengeGoal(this, EntityProtagonist.class));
        this.targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, true, living -> living instanceof Affiliated && ((Affiliated) living).getAffiliation() != Constants.Affiliation.NONE));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, true, player -> player instanceof ISanity && ((ISanity) player).getSanity() <= CommonProxy.CONFIG.protagonistAggressionThreshold));
        super.initGoals();
    }

    @Override
    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    @Override
    public boolean canPickUp(ItemStack stack) {
        return super.canPickUp(stack);
    }

    @Override
    public boolean tryAttack(Entity target) {
        return super.tryAttack(target);
    }

    @Nullable
    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        setCanPickUpLoot(true);
        dataTracker.set(VARIANT, random.nextInt(4));
        initEquipment(difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected boolean prefersNewEquipment(ItemStack newStack, ItemStack oldStack) {
        //override etc.
        return super.prefersNewEquipment(newStack, oldStack);
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        //select based on stage
        super.initEquipment(difficulty);
        alternateWeapon = new ItemStack(Items.CROSSBOW);
        setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
        super.equipStack(slot, stack);
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
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

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.VARIANT, getVariant());
        tag.putInt(Constants.NBT.STAGE, getStage());
        CompoundTag alternateWeaponTag = new CompoundTag();
        alternateWeapon.toTag(alternateWeaponTag);
        tag.put(ALTERNATE_WEAPON, alternateWeaponTag);
        tag.putBoolean(Constants.NBT.CHARGING, dataTracker.get(LOADING));
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
        setStage(tag.getInt(Constants.NBT.STAGE));
        alternateWeapon = ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.ALTERNATE_WEAPON));
        setCharging(tag.getBoolean(Constants.NBT.CHARGING));
    }

    public void switchWeapons() {
        if (!alternateWeapon.isEmpty()) {
            ItemStack stack = getMainHandStack();
            setStackInHand(Hand.MAIN_HAND, alternateWeapon);
            alternateWeapon = stack;
        }
    }

    private boolean isValidRangedItem(Item item) {
        return item instanceof ItemGun || item instanceof RangedWeaponItem;
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

    public static class SwitchWeaponsGoal extends Goal {
        private final EntityProtagonist protagonist;

        public SwitchWeaponsGoal(EntityProtagonist protagonist) {
            this.protagonist = protagonist;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
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
            if (protagonist.isValidRangedItem(protagonist.getMainHandStack().getItem())) {
                return protagonist.alternateWeapon.getItem() instanceof SwordItem && protagonist.distanceTo(protagonist.getTarget()) < 5;
            }
            return protagonist.distanceTo(protagonist.getTarget()) > 8 && protagonist.isValidRangedItem(protagonist.alternateWeapon.getItem());
        }
    }

    public static class ProtagonistCrossbowAttackGoal extends Goal {
        public static final IntRange field_25696 = new IntRange(20, 40);
        private final EntityProtagonist actor;
        private Stage stage;
        private final double speed;
        private final float squaredRange;
        private int seeingTargetTicker;
        private int chargedTicksLeft;
        private int field_25697;

        public ProtagonistCrossbowAttackGoal(EntityProtagonist actor, double speed, float range) {
            this.stage = Stage.UNCHARGED;
            this.actor = actor;
            this.speed = speed;
            this.squaredRange = range * range;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        }

        public boolean canStart() {
            return this.hasAliveTarget() && this.isEntityHoldingCrossbow();
        }

        private boolean isEntityHoldingCrossbow() {
            return this.actor.isHolding(Items.CROSSBOW);
        }

        public boolean shouldContinue() {
            return this.hasAliveTarget() && (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isEntityHoldingCrossbow();
        }

        private boolean hasAliveTarget() {
            return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
        }

        public void stop() {
            super.stop();
            this.actor.setAttacking(false);
            this.actor.setTarget(null);
            this.seeingTargetTicker = 0;
            if (this.actor.isUsingItem()) {
                this.actor.clearActiveItem();
                ((CrossbowUser) this.actor).setCharging(false);
                CrossbowItem.setCharged(this.actor.getActiveItem(), false);
            }

        }

        public void tick() {
            LivingEntity livingEntity = this.actor.getTarget();
            if (livingEntity != null) {
                boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
                boolean bl2 = this.seeingTargetTicker > 0;
                if (bl != bl2) {
                    this.seeingTargetTicker = 0;
                }

                if (bl) {
                    ++this.seeingTargetTicker;
                } else {
                    --this.seeingTargetTicker;
                }

                double d = this.actor.squaredDistanceTo(livingEntity);
                boolean bl3 = (d > (double) this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
                if (bl3) {
                    --this.field_25697;
                    if (this.field_25697 <= 0) {
                        this.actor.getNavigation().startMovingTo(livingEntity, this.isUncharged() ? this.speed : this.speed * 0.5D);
                        this.field_25697 = field_25696.choose(this.actor.getRandom());
                    }
                } else {
                    this.field_25697 = 0;
                    this.actor.getNavigation().stop();
                }

                this.actor.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
                if (this.stage == Stage.UNCHARGED) {
                    if (!bl3) {
                        this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
                        this.stage = Stage.CHARGING;
                        ((CrossbowUser) this.actor).setCharging(true);
                    }
                } else if (this.stage == Stage.CHARGING) {
                    if (!this.actor.isUsingItem()) {
                        this.stage = Stage.UNCHARGED;
                    }

                    int i = this.actor.getItemUseTime();
                    ItemStack itemStack = this.actor.getActiveItem();
                    if (i >= CrossbowItem.getPullTime(itemStack)) {
                        this.actor.stopUsingItem();
                        this.stage = Stage.CHARGED;
                        this.chargedTicksLeft = 20 + this.actor.getRandom().nextInt(20);
                        ((CrossbowUser) this.actor).setCharging(false);
                    }
                } else if (this.stage == Stage.CHARGED) {
                    --this.chargedTicksLeft;
                    if (this.chargedTicksLeft == 0) {
                        this.stage = Stage.READY_TO_ATTACK;
                    }
                } else if (this.stage == Stage.READY_TO_ATTACK && bl) {
                    ((RangedAttackMob) this.actor).attack(livingEntity, 1.0F);
                    ItemStack itemStack2 = this.actor.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
                    CrossbowItem.setCharged(itemStack2, false);
                    this.stage = Stage.UNCHARGED;
                }

            }
        }

        private boolean isUncharged() {
            return this.stage == Stage.UNCHARGED;
        }

        static enum Stage {
            UNCHARGED,
            CHARGING,
            CHARGED,
            READY_TO_ATTACK;
        }
    }

    //todo, also crossbow
    public static class ProtagonistBowAttackGoal extends BowAttackGoal {
        private final EntityProtagonist actor;

        public ProtagonistBowAttackGoal(EntityProtagonist actor, double speed, int attackInterval, float range) {
            super(actor, speed, attackInterval, range);
            this.actor = actor;
        }

        public boolean shouldContinue() {
            return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isHoldingBow();
        }
    }
}

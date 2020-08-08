package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.ModObjects;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.EnumSet;

import static com.miskatonicmysteries.lib.util.Constants.NBT.ALTERNATE_WEAPON;

public class EntityProtagonist extends HostileEntity implements RangedAttackMob, CrossbowUser {
    private static final TrackedData<Boolean> LOADING = DataTracker.registerData(EntityProtagonist.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityProtagonist.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> STAGE = DataTracker.registerData(EntityProtagonist.class, TrackedDataHandlerRegistry.INTEGER);
    public ItemStack alternateWeapon = ItemStack.EMPTY;

    public EntityProtagonist(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, 0);
        dataTracker.startTracking(STAGE, 0);
        dataTracker.startTracking(LOADING, false);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        //ai to switch to more convenient weapon if needed
        this.goalSelector.add(1, new SwitchWeaponsGoal(this));
        this.goalSelector.add(2, new ProtagonistGunAttackGoal(this));
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
        dataTracker.set(STAGE, random.nextInt(4));
        initEquipment(difficulty);
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    @Override
    protected boolean prefersNewEquipment(ItemStack newStack, ItemStack oldStack) {
        return oldStack.isEmpty() && (isValidRangedItem(newStack.getItem()) || newStack.getItem() instanceof SwordItem);
    }

    @Override
    protected void initEquipment(LocalDifficulty difficulty) {
        super.initEquipment(difficulty);
        alternateWeapon = new ItemStack(ModObjects.RIFLE);
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
            if (protagonist.getMainHandStack().isEmpty()) return true;

            if (protagonist.isValidRangedItem(protagonist.getMainHandStack().getItem())) {
                return protagonist.alternateWeapon.getItem() instanceof SwordItem && protagonist.distanceTo(protagonist.getTarget()) < 4;
            }
            return protagonist.distanceTo(protagonist.getTarget()) > 6 && protagonist.isValidRangedItem(protagonist.alternateWeapon.getItem());
        }
    }

    public static class ProtagonistGunAttackGoal extends Goal {
        public static final int RANGE = 10;
        private final EntityProtagonist protagonist;
        private int ticksToLockIn, ticksSeen;
        private boolean movingToLeft;
        private boolean backward;
        private int combatTicks = -1;

        public ProtagonistGunAttackGoal(EntityProtagonist protagonist) {
            this.protagonist = protagonist;
            this.setControls(EnumSet.of(Goal.Control.LOOK, Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return protagonist.getTarget() != null && protagonist.isHolding(item -> item instanceof ItemGun);
        }

        @Override
        public boolean shouldContinue() {
            return canStart();
        }

        @Override
        public void start() {
            protagonist.setAttacking(true);
        }

        @Override
        public void stop() {
            protagonist.setAttacking(false);
            protagonist.clearActiveItem();
        }

        @Override
        public void tick() {
            ItemStack gun = protagonist.getMainHandStack();
            LivingEntity target = protagonist.getTarget();
            double distanceToTarget = this.protagonist.distanceTo(target);
            double range = 10;
            float speed = 1.2F;
            protagonist.lookAtEntity(target, 45F, 45F);
            if (protagonist.canSee(target)) ticksSeen++;
            if (distanceToTarget <= range && this.ticksSeen >= 20) {
                this.protagonist.getNavigation().stop();
                ++this.combatTicks;
            } else {
                this.protagonist.getNavigation().startMovingTo(target, speed);
                this.combatTicks = -1;
            }

            if (this.combatTicks >= 20) {
                if ((double) this.protagonist.getRandom().nextFloat() < 0.3D) {
                    this.movingToLeft = !this.movingToLeft;
                }

                if ((double) this.protagonist.getRandom().nextFloat() < 0.3D) {
                    this.backward = !this.backward;
                }

                this.combatTicks = 0;
            }

            if (this.combatTicks > -1) {
                if (distanceToTarget > RANGE * 0.75F) {
                    this.backward = false;
                } else if (distanceToTarget < RANGE * 0.25F) {
                    this.backward = true;
                }
                this.protagonist.getMoveControl().strafeTo(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);


                if (!gun.isEmpty() && gun.getItem() instanceof ItemGun && protagonist.getTarget() != null) {
                    if (ItemGun.isLoaded(gun)) {
                        ticksToLockIn++;
                        if (ticksToLockIn >= 20) {
                            protagonist.clearActiveItem();
                            ((ItemGun) gun.getItem()).shoot(protagonist.world, protagonist, gun);
                            ticksToLockIn = 0;
                        }
                    } else {
                        ItemGun.setLoading(gun, true);
                        protagonist.setCurrentHand(Hand.MAIN_HAND);
                    }
                }
            }
        }
    }

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

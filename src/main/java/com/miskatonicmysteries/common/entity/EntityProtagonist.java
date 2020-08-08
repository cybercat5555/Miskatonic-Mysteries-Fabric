package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
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

import static com.miskatonicmysteries.lib.util.Constants.DataTrackers.STAGE;
import static com.miskatonicmysteries.lib.util.Constants.DataTrackers.VARIANT;
import static com.miskatonicmysteries.lib.util.Constants.NBT.ALTERNATE_WEAPON;

public class EntityProtagonist extends MobEntityWithAi {
    public ItemStack alternateWeapon = ItemStack.EMPTY;
    public MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2D, false);
    public ProtagonistRangedAttackGoal rangedAttackGoal = new ProtagonistRangedAttackGoal(this, 1.2D, 20, 24);

    public EntityProtagonist(EntityType<? extends MobEntityWithAi> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, 0);
        dataTracker.startTracking(STAGE, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        //ai to switch to more convenient weapon if needed
        this.goalSelector.add(1, new SwitchWeaponsGoal(this));
        this.goalSelector.add(2, new ProtagonistRangedAttackGoal(this, 1.2F, 20, 24));
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
        alternateWeapon = new ItemStack(Items.BOW);
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
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
        setStage(tag.getInt(Constants.NBT.STAGE));
        alternateWeapon = ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.ALTERNATE_WEAPON));
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

    //todo, also crossbow
    public static class ProtagonistRangedAttackGoal extends Goal {
        private final EntityProtagonist actor;
        private final double speed;
        private int attackInterval;
        private final float squaredRange;
        private int cooldown = -1;
        private int targetSeeingTicker;
        private boolean movingToLeft;
        private boolean backward;
        private int combatTicks = -1;

        public ProtagonistRangedAttackGoal(EntityProtagonist actor, double speed, int attackInterval, float range) {
            this.actor = actor;
            this.speed = speed;
            this.attackInterval = attackInterval;
            this.squaredRange = range * range;
        }

        public void setAttackInterval(int attackInterval) {
            this.attackInterval = attackInterval;
        }

        public boolean canStart() {
            return this.actor.getTarget() != null & this.holdsValidWeapon();
        }

        protected boolean holdsValidWeapon() {
            return actor.isValidRangedItem(actor.getMainHandStack().getItem());
        }

        public boolean shouldContinue() {
            return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.holdsValidWeapon();
        }

        public void start() {
            super.start();
            this.actor.setAttacking(true);
        }

        public void stop() {
            super.stop();
            this.actor.setAttacking(false);
            this.targetSeeingTicker = 0;
            this.cooldown = -1;
            this.actor.clearActiveItem();
        }

        public void tick() {
            LivingEntity livingEntity = this.actor.getTarget();
            if (livingEntity != null) {
                double d = this.actor.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
                boolean bl2 = this.targetSeeingTicker > 0;
                if (bl != bl2) {
                    this.targetSeeingTicker = 0;
                }

                if (bl) {
                    ++this.targetSeeingTicker;
                } else {
                    --this.targetSeeingTicker;
                }

                if (d <= (double) this.squaredRange && this.targetSeeingTicker >= 20) {
                    this.actor.getNavigation().stop();
                    ++this.combatTicks;
                } else {
                    this.actor.getNavigation().startMovingTo(livingEntity, this.speed);
                    this.combatTicks = -1;
                }

                if (this.combatTicks >= 20) {
                    if ((double) this.actor.getRandom().nextFloat() < 0.3D) {
                        this.movingToLeft = !this.movingToLeft;
                    }

                    if ((double) this.actor.getRandom().nextFloat() < 0.3D) {
                        this.backward = !this.backward;
                    }

                    this.combatTicks = 0;
                }

                if (this.combatTicks > -1) {
                    if (d > (double) (this.squaredRange * 0.75F)) {
                        this.backward = false;
                    } else if (d < (double) (this.squaredRange * 0.25F)) {
                        this.backward = true;
                    }

                    this.actor.getMoveControl().strafeTo(this.backward ? -0.5F : 0.5F, this.movingToLeft ? 0.5F : -0.5F);
                    this.actor.lookAtEntity(livingEntity, 30.0F, 30.0F);
                } else {
                    this.actor.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
                }

                if (this.actor.isUsingItem()) {
                    if (!bl && this.targetSeeingTicker < -60) {
                        this.actor.clearActiveItem();
                    } else if (bl) {
                        int i = this.actor.getItemUseTime();
                        if (i >= 20) {
                            this.actor.clearActiveItem();
                            attack(actor, livingEntity, BowItem.getPullProgress(i));
                            this.cooldown = this.attackInterval;
                        }
                    }
                } else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
                    this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.BOW));
                }

            }
        }

        public void attack(LivingEntity user, LivingEntity target, float pullProgress) {
            ItemStack itemStack = user.getArrowType(user.getStackInHand(ProjectileUtil.getHandPossiblyHolding(user, Items.BOW)));
            PersistentProjectileEntity persistentProjectileEntity = ProjectileUtil.createArrowProjectile(user, itemStack, pullProgress);
            double d = target.getX() - user.getX();
            double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
            double f = target.getZ() - user.getZ();
            double g = MathHelper.sqrt(d * d + f * f);
            persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float) (14 - user.world.getDifficulty().getId() * 4));
            user.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (user.getRandom().nextFloat() * 0.4F + 0.8F));
            user.world.spawnEntity(persistentProjectileEntity);
        }
    }
}

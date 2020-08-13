package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;

public class EntityHasturCultist extends MobEntityWithAi {
    protected static final TrackedData<Integer> VARIANT = DataTracker.registerData(EntityProtagonist.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Boolean> ASCENDED = DataTracker.registerData(EntityProtagonist.class, TrackedDataHandlerRegistry.BOOLEAN);

    public EntityHasturCultist(EntityType<? extends MobEntityWithAi> entityType, World world) {
        super(entityType, world);
        ((MobNavigation) this.getNavigation()).setCanPathThroughDoors(true);
        this.getNavigation().setCanSwim(true);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new LongDoorInteractGoal(this, false));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 1D, false));
        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(4, new LookAroundGoal(this));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 12));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.targetSelector.add(0, new RevengeGoal(this, EntityProtagonist.class));
        this.targetSelector.add(1, new FollowTargetGoal<>(this, LivingEntity.class, 10, true, true, living -> living instanceof Affiliated && ((Affiliated) living).getAffiliation() == Constants.Affiliation.SHUB));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, HostileEntity.class, 5, true, true, mob -> !(mob instanceof EntityProtagonist) && !(mob instanceof CreeperEntity)));

    }

    @Nullable
    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        dataTracker.set(VARIANT, random.nextInt(2));
        if (spawnReason != SpawnReason.EVENT && spawnReason != SpawnReason.STRUCTURE)
            dataTracker.set(ASCENDED, random.nextBoolean());
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }


    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, 0);
        dataTracker.startTracking(ASCENDED, false);
    }

    public int getVariant() {
        return dataTracker.get(VARIANT);
    }

    public boolean isAscended() {
        return dataTracker.get(ASCENDED);
    }

    public void ascend(boolean ascend) {
        dataTracker.set(ASCENDED, ascend);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.VARIANT, getVariant());
        tag.putBoolean(Constants.NBT.ASCENDED, isAscended());
    }


    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
        ascend(tag.getBoolean(Constants.NBT.ASCENDED));
    }
}

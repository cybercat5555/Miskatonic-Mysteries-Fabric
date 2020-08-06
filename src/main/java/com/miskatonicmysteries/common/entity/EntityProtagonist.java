package com.miskatonicmysteries.common.entity;

import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;

import static com.miskatonicmysteries.lib.util.Constants.DataTrackers.VARIANT;

public class EntityProtagonist extends MobEntityWithAi {
    public EntityProtagonist(EntityType<? extends MobEntityWithAi> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(VARIANT, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAroundGoal(this));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 12));
        this.goalSelector.add(3, new WanderAroundGoal(this, 1.0D));
        super.initGoals();
    }

    @Nullable
    @Override
    public EntityData initialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
        dataTracker.set(VARIANT, random.nextInt(4));
        return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
    }

    public int getVariant() {
        return dataTracker.get(VARIANT);
    }

    @Override
    public void writeCustomDataToTag(CompoundTag tag) {
        super.writeCustomDataToTag(tag);
        tag.putInt(Constants.NBT.VARIANT, getVariant());
    }

    @Override
    public void readCustomDataFromTag(CompoundTag tag) {
        super.readCustomDataFromTag(tag);
        dataTracker.set(VARIANT, tag.getInt(Constants.NBT.VARIANT));
    }
}

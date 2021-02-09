package com.miskatonicmysteries.common.block.blockentity.energy;

import com.miskatonicmysteries.common.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public class PowerCellBlockEntity extends BaseBlockEntity implements Tickable, EnergyStorage {
    public PowerCellBlockEntity() {
        super(MMObjects.POWER_CELL_BLOCK_ENTITY_TYPE);
    }

    private double energy;
    public static final int MAX_STORAGE = 32000;

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putDouble(Constants.NBT.ENERGY, energy);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        energy = tag.getDouble(Constants.NBT.ENERGY);
        super.fromTag(state, tag);
    }


    @Override
    public void tick() {
        if (getCachedState().get(Properties.UP)) {
            BlockEntity storage = world.getBlockEntity(pos.up());
            if (storage instanceof EnergyStorage) {
                Energy.of(this).into(Energy.of(storage)).move();
                world.updateComparators(pos, MMObjects.POWER_CELL);
            }
        }
    }

    @Override
    public double getStored(EnergySide face) {
        return energy;
    }

    @Override
    public void setStored(double amount) {
        this.energy = amount;
    }

    @Override
    public double getMaxStoredPower() {
        return MAX_STORAGE;
    }

    @Override
    public EnergyTier getTier() {
        return EnergyTier.LOW;
    }

    @Override
    public double getMaxInput(EnergySide side) {
        return 0;
    }

    @Override
    public double getMaxOutput(EnergySide side) {
        return side != EnergySide.UP ? EnergyTier.LOW.getMaxOutput() : 0;
    }
}

package com.miskatonicmysteries.common.block.blockentity.energy;

import com.miskatonicmysteries.common.block.PowerCellBlock;
import com.miskatonicmysteries.common.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
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
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putDouble(Constants.NBT.ENERGY, energy);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(BlockState state, NbtCompound tag) {
        energy = tag.getDouble(Constants.NBT.ENERGY);
        super.readNbt(state, tag);
    }


    @Override
    public void tick() {
        for (Direction direction : PowerCellBlock.DIRECTION_PROPERTY_MAP.keySet()) {
            if (getCachedState().get(PowerCellBlock.DIRECTION_PROPERTY_MAP.get(direction))) {
                BlockEntity storage = world.getBlockEntity(pos.offset(direction));
                if (storage instanceof EnergyStorage) {
                    Energy.of(this).into(Energy.of(storage)).move();
                }
            }
        }

    }

    @Override
    public void markDirty() {
        if (world != null) {
            world.updateComparators(pos, MMObjects.POWER_CELL);
        }
        super.markDirty();
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

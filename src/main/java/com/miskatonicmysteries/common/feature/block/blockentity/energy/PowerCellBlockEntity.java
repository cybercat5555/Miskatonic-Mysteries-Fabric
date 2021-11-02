package com.miskatonicmysteries.common.feature.block.blockentity.energy;

import com.miskatonicmysteries.common.feature.block.PowerCellBlock;
import com.miskatonicmysteries.common.feature.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import team.reborn.energy.Energy;
import team.reborn.energy.EnergySide;
import team.reborn.energy.EnergyStorage;
import team.reborn.energy.EnergyTier;

public class PowerCellBlockEntity extends BaseBlockEntity implements EnergyStorage {

	public static final int MAX_STORAGE = 32000;
	private double energy;

	public PowerCellBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.POWER_CELL_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static void tick(PowerCellBlockEntity blockEntity) {
		for (Direction direction : PowerCellBlock.DIRECTION_PROPERTY_MAP.keySet()) {
			if (blockEntity.getCachedState().get(PowerCellBlock.DIRECTION_PROPERTY_MAP.get(direction))) {
				BlockEntity storage = blockEntity.world.getBlockEntity(blockEntity.pos.offset(direction));
				if (storage instanceof EnergyStorage) {
					Energy.of(blockEntity).into(Energy.of(storage)).move();
				}
			}
		}

	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.putDouble(Constants.NBT.ENERGY, energy);
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		energy = tag.getDouble(Constants.NBT.ENERGY);
		super.readNbt(tag);
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

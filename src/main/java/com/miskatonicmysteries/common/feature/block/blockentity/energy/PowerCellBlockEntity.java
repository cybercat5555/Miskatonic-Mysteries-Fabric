package com.miskatonicmysteries.common.feature.block.blockentity.energy;

import com.miskatonicmysteries.common.feature.block.PowerCellBlock;
import com.miskatonicmysteries.common.feature.block.blockentity.BaseBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.api.base.SimpleSidedEnergyContainer;

public class PowerCellBlockEntity extends BaseBlockEntity {
	public static final int MAX_STORAGE = 64000;
	public final SimpleSidedEnergyContainer energyStorage = new SimpleSidedEnergyContainer() {
		@Override
		public long getCapacity() {
			return MAX_STORAGE;
		}

		@Override
		public long getMaxInsert(@Nullable Direction side) {
			return 0;
		}

		@Override
		public long getMaxExtract(@Nullable Direction side) {
			return side != Direction.UP ? 100 : 0; //2 seconds (40 ticks) of extraction = one burnt coal (4000)
		}

		@Override
		protected void onFinalCommit() {
			markDirty();
		}
	};

	public PowerCellBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.POWER_CELL_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static void tick(PowerCellBlockEntity blockEntity) {
		for (Direction direction : PowerCellBlock.DIRECTION_PROPERTY_MAP.keySet()) {
			if (blockEntity.getCachedState().get(PowerCellBlock.DIRECTION_PROPERTY_MAP.get(direction))) {
				EnergyStorage sided = EnergyStorage.SIDED.find(blockEntity.world, blockEntity.pos.offset(direction), direction.getOpposite());
				EnergyStorageUtil.move(blockEntity.energyStorage.getSideStorage(direction), sided, Long.MAX_VALUE, null);
			}
		}

	}

	@Override
	public void writeNbt(NbtCompound tag) {
		tag.putLong(Constants.NBT.ENERGY, energyStorage.amount);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		energyStorage.amount = tag.getLong(Constants.NBT.ENERGY);
		super.readNbt(tag);
	}

	@Override
	public void markDirty() {
		if (world != null) {
			world.updateComparators(pos, MMObjects.POWER_CELL);
		}
		super.markDirty();
	}
}

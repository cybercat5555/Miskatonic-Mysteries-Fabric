package com.miskatonicmysteries.common.block.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;

public abstract class BaseBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public BaseBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void fromClientTag(NbtCompound compoundTag) {
        readNbt(world.getBlockState(pos), compoundTag);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound compoundTag) {
        writeNbt(compoundTag);
        return compoundTag;
    }
}

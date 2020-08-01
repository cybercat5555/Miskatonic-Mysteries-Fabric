package com.miskatonicmysteries.common.block.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;

import javax.annotation.Nullable;
public abstract class BlockEntityBase extends BlockEntity implements BlockEntityClientSerializable {
    public BlockEntityBase(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(world.getBlockState(pos), compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        toTag(compoundTag);
        return compoundTag;
    }


    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(pos, -1, toClientTag(new CompoundTag()));
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!world.isClient) sync();
    }
}

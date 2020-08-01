package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.block.BlockStatue;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.lib.ModObjects;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

public class BlockEntityStatue extends BlockEntityBase implements Affiliated {
    public BlockEntityStatue() {
        super(ModObjects.STATUE_BLOCK_ENTITY_TYPE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
    }

    @Override
    public Identifier getAffiliation() {
        return world.getBlockState(pos).getBlock() instanceof BlockStatue ? ((BlockStatue) world.getBlockState(pos).getBlock()).getAffiliation() : null;
    }

    @Override
    public boolean isSupernatural() {
        return world.getBlockState(pos).getBlock() instanceof BlockStatue && ((BlockStatue) world.getBlockState(pos).getBlock()).isSupernatural();
    }
}

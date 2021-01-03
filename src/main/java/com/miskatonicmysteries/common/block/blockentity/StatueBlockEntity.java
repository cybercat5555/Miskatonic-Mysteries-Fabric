package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.block.StatueBlock;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.lib.ModObjects;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;

public class StatueBlockEntity extends BaseBlockEntity implements Affiliated {
    public StatueBlockEntity() {
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
    public Affiliation getAffiliation() {
        return world.getBlockState(pos).getBlock() instanceof StatueBlock ? ((StatueBlock) world.getBlockState(pos).getBlock()).getAffiliation() : null;
    }

    @Override
    public boolean isSupernatural() {
        return world.getBlockState(pos).getBlock() instanceof StatueBlock && ((StatueBlock) world.getBlockState(pos).getBlock()).isSupernatural();
    }
}

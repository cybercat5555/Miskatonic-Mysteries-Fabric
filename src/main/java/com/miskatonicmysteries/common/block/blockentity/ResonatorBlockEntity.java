package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.lib.MMObjects;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class ResonatorBlockEntity extends BaseBlockEntity implements Tickable {
    public ResonatorBlockEntity() {
        super(MMObjects.RESONATOR_BLOCK_ENTITY_TYPE);
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
    public void tick() {

    }
}

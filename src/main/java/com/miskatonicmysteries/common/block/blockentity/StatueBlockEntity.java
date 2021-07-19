package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class StatueBlockEntity extends BaseBlockEntity implements Affiliated {
    public UUID creator = null;
    public String creatorName;

    public StatueBlockEntity(BlockPos pos, BlockState state) {
        super(MMObjects.STATUE_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (creator != null) {
            tag.putUuid(Constants.NBT.PLAYER_UUID, creator);
        }
        if (creatorName != null) {
            tag.putString(Constants.NBT.PLAYER_NAME, creatorName);
        }
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        if (tag.contains(Constants.NBT.PLAYER_UUID)) {
            creator = tag.getUuid(Constants.NBT.PLAYER_UUID);
        }
        if (tag.contains(Constants.NBT.PLAYER_NAME)) {
            creatorName = tag.getString(Constants.NBT.PLAYER_NAME);
        }
        super.readNbt(tag);
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return world.getBlockState(pos).getBlock() instanceof StatueBlock ? ((StatueBlock) world.getBlockState(pos).getBlock()).getAffiliation(apparent) : MMAffiliations.NONE;
    }

    @Override
    public boolean isSupernatural() {
        return world.getBlockState(pos).getBlock() instanceof StatueBlock && ((StatueBlock) world.getBlockState(pos).getBlock()).isSupernatural();
    }
}

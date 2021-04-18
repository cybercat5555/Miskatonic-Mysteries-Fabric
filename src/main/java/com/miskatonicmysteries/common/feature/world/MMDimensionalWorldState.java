package com.miskatonicmysteries.common.feature.world;

import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashSet;
import java.util.Set;

import static com.miskatonicmysteries.common.util.Constants.NBT.WARDING_MARKS;

public class MMDimensionalWorldState extends PersistentState {
    private final Set<BlockPos> wardingMarks = new HashSet<>();
    public MMDimensionalWorldState() {
        super(Constants.MOD_ID  + "_dimensional");
    }

    public void addMark(BlockPos markPos) {
        wardingMarks.add(markPos);
        markDirty();
    }

    public void removeMark(BlockPos markPos) {
        wardingMarks.remove(markPos);
        markDirty();
    }

    public boolean isMarkNear(BlockPos pos, int radius){
        for (BlockPos wardingMark : wardingMarks) {
            if (wardingMark.isWithinDistance(pos, radius)){
                return true;
            }
        }
        return false;
    }
    @Override
    public void fromTag(CompoundTag tag) {
        ListTag wardingMarksList = (ListTag) tag.get(WARDING_MARKS);
        if (wardingMarksList != null) {
            for (Tag blockTag : wardingMarksList) {
                wardingMarks.add(NbtHelper.toBlockPos( (CompoundTag) blockTag));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag wardingMarksList = new ListTag();
        for (BlockPos wardingMark : wardingMarks) {
            wardingMarksList.add(NbtHelper.fromBlockPos(wardingMark));
        }
        tag.put(WARDING_MARKS, wardingMarksList);

        return tag;
    }

    public static MMDimensionalWorldState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(MMDimensionalWorldState::new, Constants.MOD_ID + "_dimensional");
    }
}

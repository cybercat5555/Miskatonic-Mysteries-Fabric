package com.miskatonicmysteries.common.feature.world;

import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.Objects;

public class MMWorldState extends PersistentState {

    public MMWorldState() {
        super(Constants.MOD_ID);
    }

    @Override
    public void fromTag(CompoundTag tag) {

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return tag;
    }

    public static MMWorldState get(World world) {
        return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(() -> new MMWorldState(), Constants.MOD_ID);
    }
}

package com.miskatonicmysteries.common.feature.block;

import com.miskatonicmysteries.api.block.SignBlock;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardingMarkBlock extends SignBlock {
    public WardingMarkBlock() {
        super(FabricBlockSettings.of(Material.CARPET, MapColor.YELLOW).noCollision().hardness(1).resistance(3F).luminance(4));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock() && world instanceof ServerWorld) {
            MMDimensionalWorldState.get((ServerWorld) world).removeMark(pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (state.getBlock() != oldState.getBlock() && world instanceof ServerWorld) {
            MMDimensionalWorldState.get((ServerWorld) world).addMark(pos);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }
}

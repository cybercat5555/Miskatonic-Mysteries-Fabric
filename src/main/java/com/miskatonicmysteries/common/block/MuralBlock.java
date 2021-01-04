package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

public class MuralBlock extends HorizontalFacingBlock implements Affiliated {
    private final Affiliation affiliation;

    public MuralBlock(Affiliation affiliation, Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
        this.affiliation = affiliation;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return affiliation;
    }

    @Override
    public boolean isSupernatural() {
        return false;
    }
}

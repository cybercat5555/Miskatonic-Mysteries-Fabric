package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.feature.Affiliation;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class StatueBlock extends Block implements Waterloggable, BlockEntityProvider, Affiliated {
    public static List<StatueBlock> STATUES = new ArrayList<>();
    private Affiliation affiliation;
    private static final VoxelShape SHAPE = createCuboidShape(4, 0, 4, 12, 16, 12);

    public StatueBlock(Affiliation affiliation, Settings settings) {
        super(settings.nonOpaque());
        this.affiliation = affiliation;
        setDefaultState(getDefaultState().with(Properties.ROTATION, 0));
        STATUES.add(this);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(Properties.ROTATION, rotation.rotate(state.get(Properties.ROTATION), 16));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(Properties.ROTATION, mirror.mirror(state.get(Properties.ROTATION), 16));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }


    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = this.getDefaultState().with(Properties.ROTATION, MathHelper.floor((double) (ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15);
        if (state.contains(WATERLOGGED)) {
            final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
            return state.with(WATERLOGGED, source);
        }
        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(Properties.ROTATION, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new StatueBlockEntity();
    }

    @Override
    public Affiliation getAffiliation() {
        return affiliation;
    }

    @Override
    public boolean isSupernatural() {
        return true;
    }
}

package com.miskatonicmysteries.common.feature.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;

import org.jetbrains.annotations.Nullable;

public class ElderianStairsBlock extends StairsBlock {

	public static final IntProperty VARIANTS = IntProperty.of("variants", 0, 1);

	public ElderianStairsBlock(Block block, Settings settings) {
		super(block.getDefaultState(), settings);
		this.setDefaultState((((((this.stateManager.getDefaultState()).with(VARIANTS, 0)).with(FACING, Direction.NORTH)).with(HALF, BlockHalf.BOTTOM))
			.with(SHAPE, StairShape.STRAIGHT)).with(WATERLOGGED, false));

	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(VARIANTS, ctx.getWorld().random.nextInt(2));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(VARIANTS, FACING, HALF, SHAPE, WATERLOGGED);
	}
}

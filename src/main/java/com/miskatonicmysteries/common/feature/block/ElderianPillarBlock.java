package com.miskatonicmysteries.common.feature.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;

import org.jetbrains.annotations.Nullable;

public class ElderianPillarBlock extends PillarBlock {

	public static final IntProperty VARIANTS = IntProperty.of("variants", 0, 1);

	public ElderianPillarBlock(Settings settings) {
		super(settings);
		this.setDefaultState((BlockState) this.getDefaultState().with(AXIS, Direction.Axis.Y));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(VARIANTS, AXIS);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(VARIANTS, ctx.getWorld().random.nextInt(2)).with(AXIS, ctx.getSide().getAxis());
	}


}

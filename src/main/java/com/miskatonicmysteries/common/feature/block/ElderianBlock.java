package com.miskatonicmysteries.common.feature.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;

import org.jetbrains.annotations.Nullable;

public class ElderianBlock extends Block {

	public static final IntProperty VARIANTS = IntProperty.of("variants", 0, 1);

	public ElderianBlock(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return super.getPlacementState(ctx).with(VARIANTS, ctx.getWorld().random.nextInt(2));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(VARIANTS);
	}


}

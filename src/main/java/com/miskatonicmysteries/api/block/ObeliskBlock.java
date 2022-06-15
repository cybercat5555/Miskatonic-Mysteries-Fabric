package com.miskatonicmysteries.api.block;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.block.blockentity.ObeliskBlockEntity;
import com.miskatonicmysteries.common.registry.MMObjects;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallBannerBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;

import javax.annotation.Nullable;
import static net.minecraft.state.property.Properties.WATERLOGGED;

public class ObeliskBlock extends HorizontalFacingBlock implements Waterloggable, BlockEntityProvider, Affiliated {

	@Nullable
	private static BlockPattern hasturObeliskPattern;
	private final Affiliation affiliation;

	public ObeliskBlock(Affiliation affiliation, Settings settings) {
		super(settings.nonOpaque());
		this.affiliation = affiliation;
		setDefaultState(getDefaultState().with(Properties.AGE_2, 0));
	}

	public static BlockPattern getHasturObeliskPattern() {
		if (hasturObeliskPattern != null) {
			return hasturObeliskPattern;
		}
		hasturObeliskPattern = BlockPatternBuilder.start().aisle(
			"???",
			"?b?",
			"???"
		).aisle(
			"???",
			"?m?",
			"???"
		).aisle(
			"?e?",
			"sbn",
			"?w?"
		)
			.where('b', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.STONE_BRICKS)))
			.where('m', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(MMObjects.STONE_HASTUR_MURAL)))
			.where('n', CachedBlockPosition.matchesBlockState(
				BlockStatePredicate.forBlock(Blocks.YELLOW_WALL_BANNER).with(WallBannerBlock.FACING, i -> i == Direction.NORTH)))
			.where('e', CachedBlockPosition.matchesBlockState(
				BlockStatePredicate.forBlock(Blocks.YELLOW_WALL_BANNER).with(WallBannerBlock.FACING, i -> i == Direction.EAST)))
			.where('s', CachedBlockPosition.matchesBlockState(
				BlockStatePredicate.forBlock(Blocks.YELLOW_WALL_BANNER).with(WallBannerBlock.FACING, i -> i == Direction.SOUTH)))
			.where('w', CachedBlockPosition.matchesBlockState(
				BlockStatePredicate.forBlock(Blocks.YELLOW_WALL_BANNER).with(WallBannerBlock.FACING, i -> i == Direction.WEST)))
			.where('-', air -> air.getBlockState().isAir())
			.where('?', CachedBlockPosition.matchesBlockState(BlockStatePredicate.ANY)).build();
		return hasturObeliskPattern;
	}

	public static ActionResult buildObelisk(ItemUsageContext context, BlockPattern blockPattern, BlockPattern.Result result) {
		World world = context.getWorld();
		BlockPos rootPos = result.getFrontTopLeft();
		BlockPos[] bannerPoses = new BlockPos[]{rootPos.add(-1, 2, 0), rootPos.add(0, 2, -1), rootPos.add(-2, 2, -1),
			rootPos.add(-1, 2, -2)};

		//remove banners first
		for (BlockPos bannerPos : bannerPoses) {
			world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, bannerPos, Block.getRawIdFromState(world.getBlockState(bannerPos)));
			world.setBlockState(bannerPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
		}
		BlockPos.Mutable mutable = rootPos.mutableCopy();
		mutable.move(-1, 2, -1);
		for (int y = 0; y < blockPattern.getHeight(); y++) {
			world.syncWorldEvent(WorldEvents.BLOCK_BROKEN, mutable, Block.getRawIdFromState(world.getBlockState(mutable)));
			world.setBlockState(mutable, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
			mutable.move(Direction.DOWN);
		}
		BlockState state = MMObjects.HASTUR_OBELISK.getPlacementState(new ItemPlacementContext(context)).with(Properties.AGE_2, 0);
		world.setBlockState(mutable.up(), state);
		state.getBlock().onPlaced(world, mutable, state, context.getPlayer(), context.getStack());
		return ActionResult.SUCCESS;
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		super.onBroken(world, pos, state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final BlockState state = getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
		if (state.contains(WATERLOGGED)) {
			final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
			final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
			return state.with(WATERLOGGED, source);
		}
		return state;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
						 ItemStack itemStack) {
		if (isCore(state)) {
			world.setBlockState(pos.up(2), state.with(Properties.AGE_2, 1));
			world.setBlockState(pos.up(3), state.with(Properties.AGE_2, 2));
		}
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(WATERLOGGED, Properties.HORIZONTAL_FACING, Properties.AGE_2);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos,
												BlockPos posFrom) {
		if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
			world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		super.onStateReplaced(state, world, pos, newState, moved);
		if (isCore(state)) {
			ObeliskBlockEntity.revertChangedBiomes(world, pos);
		}
		int part = state.get(Properties.AGE_2);
		BlockPos.Mutable mPos = pos.mutableCopy().move(0, -part, 0);
		for (int i = 0; i < 3; i++) {
			if (i != part && world.getBlockState(mPos).getBlock() instanceof ObeliskBlock) {
				world.setBlockState(mPos, getOriginalState(i));
			}
			mPos.move(Direction.UP);
		}
	}

	private boolean isCore(BlockState state) {
		return state.get(Properties.AGE_2) == 0;
	}

	private BlockState getOriginalState(int i) {
		return (i == 1 ? MMObjects.STONE_HASTUR_MURAL : Blocks.STONE_BRICKS).getDefaultState();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return isCore(state) ? new ObeliskBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return isCore(state) ? (world1, pos, state1, blockEntity) -> ObeliskBlockEntity.tick((ObeliskBlockEntity) blockEntity) : null;
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return affiliation;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}
}

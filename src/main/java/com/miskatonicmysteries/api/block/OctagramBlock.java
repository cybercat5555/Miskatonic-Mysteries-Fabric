package com.miskatonicmysteries.api.block;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.feature.recipe.rite.TeleportRite;
import com.miskatonicmysteries.common.feature.recipe.rite.Triggerable;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.TeleportEffectPacket;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMRecipes;
import com.miskatonicmysteries.common.util.InventoryUtil;
import com.miskatonicmysteries.common.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class OctagramBlock extends HorizontalFacingBlock implements BlockEntityProvider, Affiliated {

	public static final VoxelShape OUTLINE_SHAPE = createCuboidShape(0, 0, 0, 16, 0.5F, 16);
	public static List<OctagramBlock> OCTAGRAMS = new ArrayList<>();
	private final Affiliation affiliation;

	public OctagramBlock(Affiliation affiliation) {
		super(Settings.of(Material.CARPET).nonOpaque().noCollision().hardness(1).resistance(3F));
		OCTAGRAMS.add(this);
		this.affiliation = affiliation;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		if (isPlacementValid(ctx.getWorld(), ctx.getBlockPos(), ctx)) {
			for (int i = 0; i < 8; i++) {
				if (!isPlacementValid(ctx.getWorld(), ctx.getBlockPos().add(BlockOuterOctagram.getOffsetToCenterPos(i)), ctx)) {
					return null;
				}
			}
			return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
		}
		return null;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		for (int i = 0; i < 8; i++) {
			BlockPos partPos = BlockOuterOctagram.getOffsetToCenterPos(i);
			world.setBlockState(pos.add(-partPos.getX(), 0, -partPos.getZ()),
								MMObjects.OCTAGRAM_SIDES.getDefaultState().with(BlockOuterOctagram.NUMBER, i));
		}
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.appendProperties(builder);
	}

	protected boolean isPlacementValid(World world, BlockPos pos, ItemPlacementContext ctx) {
		return (world.getBlockState(pos).isAir() || world.getBlockState(pos).canReplace(ctx)) && world.getBlockState(pos.down())
			.isSideSolidFullSquare(world, pos.down(), Direction.UP);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			for (int i = 0; i < 8; i++) {
				BlockPos partPos = BlockOuterOctagram.getOffsetToCenterPos(i);
				world.setBlockState(pos.add(-partPos.getX(), 0, -partPos.getZ()), Blocks.AIR.getDefaultState());
				if (!world.isClient) {
					((ServerWorld) world)
						.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, getDefaultState()), partPos.getX(),
										partPos.getY(), partPos.getZ(), 6, 0.0D, 0.0D, 0.0D, 0.05D);
				}
			}
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof OctagramBlockEntity) {
				OctagramBlockEntity octagram = (OctagramBlockEntity) blockEntity;
				if (octagram.currentRite != null && octagram.currentRite.isPermanent((OctagramBlockEntity) blockEntity)) {
					octagram.currentRite.onCancelled(octagram);
				}
				ItemScatterer.spawn(world, pos, octagram.getItems());
				octagram.sync(world, pos);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.getBlockEntity(pos) instanceof OctagramBlockEntity octagram) {
			if (octagram.currentRite != null) {
				return ActionResult.PASS;
			}
			if (!world.isClient) {
				octagram.setOriginalCaster(player);
				octagram.sync(world, pos);
				RiteRecipe riteRecipe = MMRecipes.getRiteRecipe(octagram);
				if (riteRecipe != null) {
					if (!player.isSneaking() && riteRecipe.rite.canCast(octagram, riteRecipe)) {
						Rite rite = riteRecipe.rite;
						octagram.setTriggered(rite.shouldTriggerFromStart(octagram, player));
						octagram.currentRite = rite;
						rite.onStart(octagram);
						octagram.markDirty();
						octagram.sync(world, pos);
						octagram.sendClientInfo(riteRecipe, true);
					} else {
						octagram.sendClientInfo(riteRecipe, false);
					}
					return ActionResult.CONSUME;
				}
			}
			return ActionResult.success(world.isClient);
		}

		return ActionResult.PASS;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return super.getCameraCollisionShape(state, world, pos, context);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient && entity.age % 5 == 0 && !entity.isSneaking() && world.getBlockEntity(pos) instanceof OctagramBlockEntity) {
			OctagramBlockEntity octagram = (OctagramBlockEntity) world.getBlockEntity(pos);
			if (octagram != null && octagram.currentRite instanceof TeleportRite && octagram.isPermanentRiteActive() && octagram.tickCount == 0
				&& octagram.getBoundDimension() != null)
			{
				Direction direction = getEffectiveDirection(octagram.getBoundDimension().getBlockState(octagram.getBoundPos()),
															state.get(FACING), entity.getMovementDirection());
				BlockPos boundPos = octagram.getBoundPos().offset(direction);
				Util.teleport(octagram.getBoundDimension(), entity, boundPos.getX() + 0.5F, boundPos.getY(), boundPos.getZ() + 0.5F,
							  direction.asRotation(), entity.getPitch());
				TeleportEffectPacket.send(entity);
			}
		}
		super.onEntityCollision(state, world, pos, entity);
	}

	private static Direction getEffectiveDirection(BlockState state, Direction baseDirection, Direction directionFrom) {
		if (state != null && state.getProperties().contains(FACING)) {
			Direction directionTo = state.get(FACING);
			float rotationDifference = directionTo.asRotation() - baseDirection.asRotation();
			if (rotationDifference < 0) {
				rotationDifference += 360;
			}
			return Direction.fromRotation(directionFrom.asRotation() + rotationDifference);
		}
		return Direction.NORTH;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new OctagramBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return (world1, pos, state1, blockEntity) -> OctagramBlockEntity.tick((OctagramBlockEntity) blockEntity);
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
		return (OctagramBlockEntity) blockEntity;
	}



	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return affiliation;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}

	public static class BlockOuterOctagram extends Block {

		public static IntProperty NUMBER = IntProperty.of("number", 0, 7);

		public BlockOuterOctagram() {
			super(Settings.of(Material.CARPET).nonOpaque().noCollision().hardness(1).resistance(3F));
			setDefaultState(getDefaultState().with(NUMBER, 0));
		}

		@Override
		public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
			if (!world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP)) {
				world.breakBlock(pos.add(getOffsetToCenterPos(state.get(NUMBER))), false);
			}
			super.neighborUpdate(state, world, pos, block, fromPos, notify);
		}

		@Override
		public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
			if (!state.isOf(newState.getBlock())) {
				world.breakBlock(pos.add(getOffsetToCenterPos(state.get(NUMBER))), false);
			}
		}

		@Override
		public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
			OctagramBlockEntity octagram = getOctagram(world, pos, state);
			if (octagram == null) {
				world.breakBlock(pos, false);
				return ActionResult.FAIL;
			} else if (octagram.currentRite == null || octagram.isPermanentRiteActive()) {
				ItemStack stack = player.getStackInHand(hand);
				if (!stack.isEmpty() && octagram.isValid(state.get(NUMBER), stack) && octagram.getStack(state.get(NUMBER)).isEmpty()
					&& !octagram.isPermanentRiteActive()) {
					octagram.setStack(state.get(NUMBER), stack);
					octagram.markDirty();
					player.swingHand(hand);
					if (!world.isClient) {
						octagram.sync(world, pos);
					}
					return ActionResult.CONSUME;
				} else if (stack.isEmpty() && !octagram.getItems().isEmpty() && !octagram.getItems().get(state.get(NUMBER)).isEmpty()) {
					InventoryUtil.giveItem(world, player, octagram.removeStack(state.get(NUMBER)));
					octagram.markDirty();
					if (!world.isClient) {
						octagram.sync(world, pos);
					}
					return ActionResult.SUCCESS;
				}
			}
			return super.onUse(state, world, pos, player, hand, hit);
		}

		public static OctagramBlockEntity getOctagram(World world, BlockPos pos, BlockState state) {
			BlockEntity blockEntity = world.getBlockEntity(pos.add(getOffsetToCenterPos(state.get(NUMBER))));
			return blockEntity instanceof OctagramBlockEntity ? (OctagramBlockEntity) blockEntity : null;
		}

		public static BlockPos getOffsetToCenterPos(int index) {
			switch (index) {
				default:
				case 0:
					return new BlockPos(0, 0, 1);
				case 1:
					return new BlockPos(-1, 0, 1);
				case 2:
					return new BlockPos(-1, 0, 0);
				case 3:
					return new BlockPos(-1, 0, -1);
				case 4:
					return new BlockPos(0, 0, -1);
				case 5:
					return new BlockPos(1, 0, -1);
				case 6:
					return new BlockPos(1, 0, 0);
				case 7:
					return new BlockPos(1, 0, 1);
			}
		}

		@Override
		public BlockRenderType getRenderType(BlockState state) {
			return BlockRenderType.INVISIBLE;
		}

		@Override
		public PistonBehavior getPistonBehavior(BlockState state) {
			return PistonBehavior.DESTROY;
		}

		@Override
		public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
			return 1;
		}

		@Override
		public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
			return OUTLINE_SHAPE;
		}

		@Override
		public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
			if (!world.isClient && getOctagram(world, pos, world.getBlockState(pos)) != null) {
				OctagramBlockEntity octagram = getOctagram(world, pos, world.getBlockState(pos));
				if (octagram.currentRite instanceof Triggerable t && t.isReadyToTrigger(octagram)) {
					t.trigger(octagram, entity);
					octagram.markDirty();
					octagram.sync(world, pos);
				}
			}
			super.onEntityCollision(state, world, pos, entity);
		}

		@Override
		public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
			return true;
		}

		@Override
		public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
			//prevent weirdness
		}

		@Override
		protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
			builder.add(NUMBER);
			super.appendProperties(builder);
		}
	}
}

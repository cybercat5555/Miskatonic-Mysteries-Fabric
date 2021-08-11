package com.miskatonicmysteries.common.block;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.block.blockentity.MasterpieceStatueBlockEntity;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class MasterpieceStatueBlock extends Block implements Waterloggable, BlockEntityProvider, Affiliated {
	private static final VoxelShape PLINTH_SHAPE = VoxelShapes.union(VoxelShapes.cuboid(1 / 32F, 0, 1 / 32F,
			1 - 1 / 32F, 3 / 16F, 1 - 1 / 32F), VoxelShapes.cuboid(3 / 32F, 3 / 16F, 3 / 32F, 1 - 3 / 32F, 6 / 16F,
			1 - 3 / 32F), VoxelShapes.cuboid(3 / 16F, 6 / 16F, 3 / 16F, 1 - 3 / 16F, 2.5F, 1 - 3 / 16F));

	public MasterpieceStatueBlock(Settings settings) {
		super(settings.nonOpaque());
		setDefaultState(getDefaultState().with(Properties.ROTATION, 0).with(WATERLOGGED, false));
	}

	public static ItemStack setSculptureData(ItemStack stack, PlayerEntity creator, PlayerEntity target, int pose) {
		if (!stack.hasTag()) {
			stack.setTag(new NbtCompound());
		}
		NbtCompound blockEntityTag = stack.getTag().contains(Constants.NBT.BLOCK_ENTITY_TAG) ?
				stack.getTag().getCompound(Constants.NBT.BLOCK_ENTITY_TAG) : new NbtCompound();
		if (creator != null) {
			blockEntityTag.putString(Constants.NBT.PLAYER_NAME, creator.getName().asString());
			blockEntityTag.putUuid(Constants.NBT.PLAYER_UUID, creator.getUuid());
		}
		if (target != null) {
			NbtCompound nbtCompound = new NbtCompound();
			NbtHelper.writeGameProfile(nbtCompound, target.getGameProfile());
			blockEntityTag.put(Constants.NBT.STATUE_OWNER, nbtCompound);
		}
		blockEntityTag.putInt(Constants.NBT.POSE, pose);
		stack.getTag().put(Constants.NBT.BLOCK_ENTITY_TAG, blockEntityTag);
		return stack;
	}

	public static boolean isPlayerMade(ItemStack stack) {
		return stack.hasTag() && stack.getTag().contains(Constants.NBT.BLOCK_ENTITY_TAG) && stack.getSubTag(Constants.NBT.BLOCK_ENTITY_TAG).contains(Constants.NBT.PLAYER_NAME);
	}

	@Override
	public void appendTooltip(ItemStack stack, @org.jetbrains.annotations.Nullable BlockView world, List<Text> tooltip,
							  TooltipContext options) {
		if (stack.hasTag() && stack.getTag().contains((Constants.NBT.BLOCK_ENTITY_TAG))) {
			NbtCompound compoundTag = stack.getSubTag(Constants.NBT.BLOCK_ENTITY_TAG);
			if (compoundTag != null && compoundTag.contains(Constants.NBT.PLAYER_NAME)) {
				tooltip.add(new TranslatableText("tooltip.miskatonicmysteries.created_by",
						compoundTag.getString(Constants.NBT.PLAYER_NAME)).formatted(Formatting.GRAY));
			}
		}
		super.appendTooltip(stack, world, tooltip, options);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(Properties.ROTATION, rotation.rotate(state.get(Properties.ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.with(Properties.ROTATION, mirror.mirror(state.get(Properties.ROTATION), 16));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return PLINTH_SHAPE;
	}

	@Override
	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final BlockState state = this.getDefaultState().with(Properties.ROTATION,
				MathHelper.floor((double) (ctx.getPlayerYaw() * 16.0F / 360.0F) + 0.5D) & 15);
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
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
												WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
			world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) :
				super.getFluidState(state);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new MasterpieceStatueBlockEntity(pos, state);
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.HASTUR;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}

	public static class MasterpieceStatueBlockItem extends BlockItem{

		public MasterpieceStatueBlockItem() {
			super(MMObjects.MASTERPIECE_STATUE, new Item.Settings().group(Constants.MM_GROUP).rarity(Rarity.UNCOMMON));
		}

		@Override
		public Text getName(ItemStack stack) {
			if (stack.hasTag() && stack.getTag().contains((Constants.NBT.BLOCK_ENTITY_TAG))) {
				NbtCompound compoundTag = stack.getSubTag(Constants.NBT.BLOCK_ENTITY_TAG);
				if (compoundTag != null && compoundTag.contains(Constants.NBT.STATUE_OWNER)) {
					GameProfile profile = NbtHelper.toGameProfile(compoundTag.getCompound(Constants.NBT.STATUE_OWNER));
					return new TranslatableText(getTranslationKey() + ".named", profile.getName());
				}
			}
			return super.getName(stack);
		}
	}
}

package com.miskatonicmysteries.api.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Iterator;

import org.jetbrains.annotations.Nullable;

public class ChalkItem extends BlockItem {

	public ChalkItem(Block block, Settings setting) {
		super(block, setting);
	}

	@Override
	public ActionResult place(ItemPlacementContext context) {
		if (!context.canPlace()) {
			return ActionResult.FAIL;
		} else {
			ItemPlacementContext itemPlacementContext = this.getPlacementContext(context);
			if (itemPlacementContext == null) {
				return ActionResult.FAIL;
			} else {
				BlockState blockState = this.getPlacementState(itemPlacementContext);
				if (blockState == null) {
					return ActionResult.FAIL;
				} else if (!this.place(itemPlacementContext, blockState)) {
					return ActionResult.FAIL;
				} else {
					BlockPos blockPos = itemPlacementContext.getBlockPos();
					World world = itemPlacementContext.getWorld();
					PlayerEntity playerEntity = itemPlacementContext.getPlayer();
					ItemStack itemStack = itemPlacementContext.getStack();
					BlockState blockState2 = world.getBlockState(blockPos);
					Block block = blockState2.getBlock();
					if (block == blockState.getBlock()) {
						blockState2 = this.placeFromNbt(blockPos, world, itemStack, blockState2);
						this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
						block.onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
						if (playerEntity instanceof ServerPlayerEntity) {
							Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
						}
					}

					BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
					world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS,
									(blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
					if (playerEntity == null || !playerEntity.isCreative()) {
						if (playerEntity != null) {
							itemStack.damage(1, playerEntity, (e) -> {
							});
						}
					}

					return ActionResult.success(world.isClient);
				}
			}
		}
	}

	private BlockState placeFromNbt(BlockPos pos, World world, ItemStack stack, BlockState state) {
		BlockState blockState = state;
		NbtCompound compoundTag = stack.getNbt();
		if (compoundTag != null) {
			NbtCompound compoundTag2 = compoundTag.getCompound("BlockStateTag");
			StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();
			Iterator var9 = compoundTag2.getKeys().iterator();

			while (var9.hasNext()) {
				String string = (String) var9.next();
				Property<?> property = stateManager.getProperty(string);
				if (property != null) {
					String string2 = compoundTag2.get(string).asString();
					blockState = with(blockState, property, string2);
				}
			}
		}

		if (blockState != state) {
			world.setBlockState(pos, blockState, 2);
		}

		return blockState;
	}

	protected static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
		return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
	}

	@Override
	protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		stack.damage(1, player, ((e) -> {
			if (e != null) {
				e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
			}
		}));
		return super.postPlacement(pos, world, player, stack, state);
	}
}

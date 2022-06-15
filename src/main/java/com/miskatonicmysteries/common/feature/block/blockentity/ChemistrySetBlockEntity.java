package com.miskatonicmysteries.common.feature.block.blockentity;

import com.miskatonicmysteries.common.feature.PotentialItem;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMRecipes;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChemistrySetBlockEntity extends BaseBlockEntity implements ImplementedBlockEntityInventory {

	private final DefaultedList<ItemStack> items = DefaultedList.ofSize(6, ItemStack.EMPTY);
	private final DefaultedList<PotentialItem> potentialItems = DefaultedList.ofSize(3, PotentialItem.EMPTY);
	public int workProgress;
	public int[] smokeColor = {0, 0, 0};

	public ChemistrySetBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static void tick(ChemistrySetBlockEntity blockEntity) {
		if (blockEntity.isLit()) {
			if (blockEntity.canWork()) {
				ChemistryRecipe recipe = MMRecipes.getChemistryRecipe(blockEntity);
				blockEntity.workProgress++;
				if (blockEntity.workProgress >= 100) {
					blockEntity.world.playSound(null, blockEntity.pos, SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.6F,
												blockEntity.world.random.nextFloat() * 0.4F + 0.8F);
					blockEntity.changeSmokeColor(recipe.color);
					for (int i = 0; i < recipe.output.size(); i++) {
						blockEntity.potentialItems.set(i, recipe.output.get(i));
					}
					blockEntity.clear();
					blockEntity.finish();
					if (!blockEntity.world.isClient) {
						blockEntity.sync(blockEntity.world, blockEntity.pos);
					}
				}
			} else {
				blockEntity.finish();
				if (!blockEntity.world.isClient) {
					blockEntity.sync(blockEntity.world, blockEntity.pos);
				}
			}
			blockEntity.markDirty();
		}
	}

	private void changeSmokeColor(int color) {
		smokeColor[0] = (color >> 16) & 0xff;
		smokeColor[1] = (color >> 8) & 0xff;
		smokeColor[2] = color & 0xff;
	}

	public void finish() {
		world.setBlockState(pos, world.getBlockState(pos).with(Properties.LIT, false));
		workProgress = 0;
	}

	public boolean canWork() {
		return MMRecipes.getChemistryRecipe(this) != null;
	}

	public boolean isLit() {
		return getCachedState().get(Properties.LIT);
	}

	public void sync(World world, BlockPos pos) {
		if (world != null && !world.isClient) {
			world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
		}
	}

	@Override
	public void readNbt(NbtCompound tag) {
		items.clear();
		Inventories.readNbt(tag, items);
		NbtList listTag = tag.getList(Constants.NBT.POTENTIAL_ITEMS, 10);

		for (int i = 0; i < listTag.size(); ++i) {
			NbtCompound compoundTag = listTag.getCompound(i);
			int j = compoundTag.getByte("Slot") & 255;
			if (j >= 0 && j < potentialItems.size()) {
				potentialItems.set(j, PotentialItem.fromTag(compoundTag));
			}
		}
		workProgress = tag.getInt(Constants.NBT.WORK_PROGRESS);
		super.readNbt(tag);
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		Inventories.writeNbt(tag, items);
		NbtList potentialItemTag = new NbtList();
		for (int i = 0; i < potentialItems.size(); i++) {
			PotentialItem item = potentialItems.get(i);
			NbtCompound compoundTag = new NbtCompound();
			compoundTag.putByte("Slot", (byte) i);
			item.toTag(compoundTag);
			potentialItemTag.add(compoundTag);
		}
		tag.put(Constants.NBT.POTENTIAL_ITEMS, potentialItemTag);
		tag.putInt(Constants.NBT.WORK_PROGRESS, workProgress);
	}

	public boolean convertPotentialItem(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		for (PotentialItem potentialItem : potentialItems) {
			if (potentialItem.canRealize(stack)) {
				ItemStack realizedStack = potentialItem.realize(stack);
				potentialItems.set(potentialItems.indexOf(potentialItem), PotentialItem.EMPTY);
				if (!world.isClient) {
					world.spawnEntity(new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), realizedStack));
				}
				return true;
			}
		}
		return false;
	}

	public boolean canBeLit(PlayerEntity playerEntity) {
		if (containsPotentialItems()) {
			playerEntity.sendMessage(new TranslatableText("message.miskatonicmysteries.chemistry_set.contains_items"), true);
			return false;
		} else if (!canWork()) {
			playerEntity.sendMessage(new TranslatableText("message.miskatonicmysteries.chemistry_set.invalid_recipe"), true);
			return false;
		}
		return true;
	}

	public boolean containsPotentialItems() {
		for (PotentialItem potentialItem : potentialItems) {
			if (!potentialItem.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return items;
	}

	public DefaultedList<PotentialItem> getPotentialItems() {
		return potentialItems;
	}
}

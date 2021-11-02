package com.miskatonicmysteries.common.feature.block.blockentity;

import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class AltarBlockEntity extends BaseBlockEntity implements ImplementedBlockEntityInventory {

	private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(1, ItemStack.EMPTY);

	public AltarBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.ALTAR_BLOCK_ENTITY_TYPE, pos, state);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		Inventories.writeNbt(tag, ITEMS);
		return super.writeNbt(tag);
	}


	@Override
	public void readNbt(NbtCompound nbt) {
		ITEMS.clear();
		Inventories.readNbt(nbt, ITEMS);
		super.readNbt(nbt);
	}

	@Override
	public void markDirty() {
		if (world != null && !world.isClient) {
			sync();
		}
		super.markDirty();
	}

	@Override
	public int getMaxCountPerStack() {
		return 1;
	}

	@Override
	public DefaultedList<ItemStack> getItems() {
		return ITEMS;
	}

	@Override
	public boolean isValid(int slot, ItemStack stack) {
		return Constants.Tags.ALTAR_BOOKS.contains(stack.getItem());
	}

	public Item getBook() {
		return getStack(0).getItem();
	}
}

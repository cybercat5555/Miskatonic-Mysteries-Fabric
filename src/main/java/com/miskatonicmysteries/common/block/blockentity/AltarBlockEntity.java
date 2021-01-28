package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMObjects;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

public class AltarBlockEntity extends BaseBlockEntity implements ImplementedBlockEntityInventory {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public AltarBlockEntity() {
        super(MMObjects.ALTAR_BLOCK_ENTITY_TYPE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, ITEMS);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        ITEMS.clear();
        Inventories.fromTag(tag, ITEMS);
        super.fromTag(state, tag);
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
        return stack.getItem().isIn(Constants.Tags.ALTAR_BOOKS);
    }

    public Item getBook() {
        return getStack(0).getItem();
    }
}

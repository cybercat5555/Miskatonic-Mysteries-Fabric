package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class AltarBlockEntity extends BaseBlockEntity implements ImplementedBlockEntityInventory {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public AltarBlockEntity() {
        super(MMObjects.ALTAR_BLOCK_ENTITY_TYPE);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        Inventories.writeNbt(tag, ITEMS);
        return super.writeNbt(tag);
    }

    @Override
    public void readNbt(BlockState state, NbtCompound tag) {
        ITEMS.clear();
        Inventories.readNbt(tag, ITEMS);
        super.readNbt(state, tag);
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

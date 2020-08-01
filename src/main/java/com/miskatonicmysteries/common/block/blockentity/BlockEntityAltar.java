package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.lib.ModObjects;
import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class BlockEntityAltar extends BlockEntityBase implements ImplementedInventory, Affiliated {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public BlockEntityAltar() {
        super(ModObjects.ALTAR_BLOCK_ENTITY_TYPE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, ITEMS);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        Inventories.fromTag(tag, ITEMS);
        super.fromTag(state, tag);
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

    @Override
    public Identifier getAffiliation() {
        return getStack(0).getItem() instanceof Affiliated ? ((Affiliated) getStack(0).getItem()).getAffiliation() : null;
    }

    @Override
    public boolean isSupernatural() {
        return getStack(0).getItem() instanceof Affiliated ? ((Affiliated) getStack(0).getItem()).isSupernatural() : false;
    }
}

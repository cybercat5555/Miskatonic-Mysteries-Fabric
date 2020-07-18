package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.block.BlockChemistrySet;
import com.miskatonicmysteries.common.feature.PotentialItem;
import com.miskatonicmysteries.lib.ModObjects;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class BlockEntityChemistrySet extends BlockEntity implements ImplementedInventory, Tickable {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(5, ItemStack.EMPTY);
    private final DefaultedList<PotentialItem> POTENTIAL_ITEMS = DefaultedList.ofSize(3, PotentialItem.EMPTY);

    public BlockEntityChemistrySet() {
        super(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE);
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
    public void tick() {
        //tick, when finished set potential items for recipe (this thing can only be lit when the recipe is valid)
        /*
        After being finished, all items will be consumed, potential items will be ghost-rendered, suggestion what to collect them with
        A new recipe can always be started, though this will reset the Potential Items

        Items only render BEFORE a recipe is started, potential items also only render when no other items are inserted


         */
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return ITEMS;
    }
}

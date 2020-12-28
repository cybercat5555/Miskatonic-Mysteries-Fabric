package com.miskatonicmysteries.common.block.blockentity;

import com.miskatonicmysteries.common.block.BlockOctagram;
import com.miskatonicmysteries.common.feature.Affiliated;
import com.miskatonicmysteries.common.lib.ModObjects;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class OctagramBlockEntity extends BaseBlockEntity implements ImplementedInventory, Affiliated, Tickable {
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(8, ItemStack.EMPTY);

    //this is currently mostly altar code lul
    public OctagramBlockEntity() {
        super(ModObjects.OCTAGRAM_BLOCK_ENTITY_TYPE);
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
    public Identifier getAffiliation() {
        return world.getBlockState(pos).getBlock() instanceof BlockOctagram ? ((BlockOctagram) world.getBlockState(pos).getBlock()).getAffiliation() : null;
    }

    @Override
    public boolean isSupernatural() {
        return world.getBlockState(pos).getBlock() instanceof BlockOctagram && ((BlockOctagram) world.getBlockState(pos).getBlock()).isSupernatural();
    }

    @Override
    public void tick() {

    }
}

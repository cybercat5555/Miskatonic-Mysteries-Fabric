package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.lib.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Ingredient;

public class PotentialItem {
    public static final PotentialItem EMPTY = new PotentialItem(ItemStack.EMPTY, ItemStack.EMPTY);
    public ItemStack stackToReceive;
    public ItemStack stackToRealize;

    public PotentialItem(ItemStack stackToReceive, ItemStack stackToRealize) {
        this.stackToReceive = stackToReceive;
        this.stackToRealize = stackToRealize;
    }

    public boolean canRealize(ItemStack stack){
        return stack.getItem().equals(stackToRealize.getItem());
    }

    public CompoundTag toTag(CompoundTag tag){
        tag.put(Constants.NBT.RECEIVED_STACK, stackToReceive.toTag(new CompoundTag()));
        tag.put(Constants.NBT.REALIZED_STACK, stackToRealize.toTag(new CompoundTag()));
        return tag;
    }

    public static PotentialItem fromTag(CompoundTag tag){
        return new PotentialItem(ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.RECEIVED_STACK)), ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.REALIZED_STACK)));
    }
    public ItemStack realize(ItemStack stack){
        stack.decrement(1);
        return stackToReceive;
    }
}

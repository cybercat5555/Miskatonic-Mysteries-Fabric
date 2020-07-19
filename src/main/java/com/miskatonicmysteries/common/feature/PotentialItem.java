package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.lib.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class PotentialItem {
    public static final PotentialItem EMPTY = new PotentialItem(ItemStack.EMPTY, ItemStack.EMPTY);
    public ItemStack in;
    public ItemStack out;

    public PotentialItem(ItemStack in, ItemStack out) {
        this.in = in;
        this.out = out;
    }

    public boolean canRealize(ItemStack stack){
        return stack.getItem().equals(in.getItem());
    }

    public CompoundTag toTag(CompoundTag tag){
        tag.put(Constants.NBT.RECEIVED_STACK, in.toTag(new CompoundTag()));
        tag.put(Constants.NBT.REALIZED_STACK, out.toTag(new CompoundTag()));
        return tag;
    }


    public ItemStack realize(ItemStack stack){
        stack.decrement(1);
        return out;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("in", in)
                .append("out", out)
                .toString();
    }

    public static PotentialItem fromTag(CompoundTag tag){
        return new PotentialItem(ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.RECEIVED_STACK)), ItemStack.fromTag((CompoundTag) tag.get(Constants.NBT.REALIZED_STACK)));
    }
}

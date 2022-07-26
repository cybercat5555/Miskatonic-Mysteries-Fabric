package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;

import com.google.gson.JsonObject;

public class PotentialItem {

	public static final PotentialItem EMPTY = new PotentialItem(ItemStack.EMPTY, ItemStack.EMPTY);
	public ItemStack in;
	public ItemStack out;

	public PotentialItem(ItemStack in, ItemStack out) {
		this.in = in;
		this.out = out;
	}

	public static PotentialItem fromPacket(PacketByteBuf buf) {
		return new PotentialItem(buf.readItemStack(), buf.readItemStack());
	}

	public static PotentialItem fromJson(JsonObject jsonElement) {
		JsonObject in = JsonHelper.getObject(jsonElement, "in");
		JsonObject out = JsonHelper.getObject(jsonElement, "out");
		int count = JsonHelper.getInt(out, "count", 1);
		return new PotentialItem(new ItemStack(ShapedRecipe.getItem(in)), new ItemStack(ShapedRecipe.getItem(out), count));
	}

	public static PotentialItem fromTag(NbtCompound tag) {
		return new PotentialItem(ItemStack.fromNbt((NbtCompound) tag.get(Constants.NBT.RECEIVED_STACK)),
								 ItemStack.fromNbt((NbtCompound) tag.get(Constants.NBT.REALIZED_STACK)));
	}

	public boolean canRealize(ItemStack stack) {
		return stack.getItem().equals(in.getItem());
	}

	public ItemStack realize(ItemStack stack) {
		stack.decrement(1);
		return out;
	}

	public boolean isEmpty() {
		return in.isEmpty() && out.isEmpty();
	}

	@Override
	public String toString() {
		return new org.apache.commons.lang3.builder.ToStringBuilder(this)
			.append("in", in)
			.append("out", out)
			.toString();
	}

	public void write(PacketByteBuf buf) {
		buf.writeItemStack(in);
		buf.writeItemStack(out);
	}

	public NbtCompound toTag(NbtCompound tag) {
		tag.put(Constants.NBT.RECEIVED_STACK, in.writeNbt(new NbtCompound()));
		tag.put(Constants.NBT.REALIZED_STACK, out.writeNbt(new NbtCompound()));
		return tag;
	}
}

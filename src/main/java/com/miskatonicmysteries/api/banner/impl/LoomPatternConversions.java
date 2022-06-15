package com.miskatonicmysteries.api.banner.impl;

import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class LoomPatternConversions {

	private LoomPatternConversions() {
	}

	/**
	 * Extracts the loom pattern tag from the given ItemStack.
	 *
	 * @return the loom pattern tag, or null if it is not present.
	 */
	public static NbtList getLoomPatternTag(ItemStack stack) {
		NbtCompound tag = stack.getSubNbt("BlockEntityTag");

		if (tag != null && tag.contains(LoomPatternContainer.NBT_KEY, 9)) {
			return tag.getList(LoomPatternContainer.NBT_KEY, 10);
		} else {
			return null;
		}
	}

	/**
	 * Parses the given NBT data into a list of LoomPatternData objects.
	 *
	 * @param tag a nullable NbtList with loom pattern data
	 */
	public static List<LoomPatternData> makeLoomPatternData(NbtList tag) {
		List<LoomPatternData> res = new ArrayList<>();

		if (tag != null) {
			for (NbtElement t : tag) {
				NbtCompound patternTag = (NbtCompound) t;
				LoomPattern pattern = LoomPatterns.REGISTRY.get(new Identifier(patternTag.getString("Pattern")));

				if (pattern != null) {
					DyeColor color = DyeColor.byId(patternTag.getInt("Color"));
					int index = patternTag.getInt("Index");
					res.add(new LoomPatternData(pattern, color, index));
				}
			}
		}

		return res;
	}
}
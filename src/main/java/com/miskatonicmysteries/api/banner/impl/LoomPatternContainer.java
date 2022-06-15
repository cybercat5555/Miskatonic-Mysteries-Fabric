package com.miskatonicmysteries.api.banner.impl;

import net.minecraft.nbt.NbtList;

import java.util.List;

public interface LoomPatternContainer {

	String NBT_KEY = "Bannerpp_LoomPatterns";

	List<LoomPatternData> bannerpp_getLoomPatterns();

	/**
	 * Internal interface that allows the client mixin to communicate with the common mixin.
	 */
	interface Internal {

		NbtList bannerpp_getLoomPatternTag();

		void bannerpp_setLoomPatternTag(NbtList tag);
	}
}
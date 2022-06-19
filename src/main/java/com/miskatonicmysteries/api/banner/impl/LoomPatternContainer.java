package com.miskatonicmysteries.api.banner.impl;

import net.minecraft.nbt.NbtList;

import java.util.List;

public interface LoomPatternContainer {
    String NBT_KEY = "Bannermm_LoomPatterns";

    List<LoomPatternData> bannermm_getLoomPatterns();

    /**
     * Internal interface that allows the client mixin to communicate
     * with the common mixin.
     */
    interface Internal {
        NbtList bannermm_getLoomPatternTag();

        void bannermm_setLoomPatternTag(NbtList tag);
    }
}
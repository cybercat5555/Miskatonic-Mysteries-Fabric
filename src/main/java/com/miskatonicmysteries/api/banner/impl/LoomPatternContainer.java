package com.miskatonicmysteries.api.banner.impl;

import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.minecraft.nbt.NbtList;

import java.util.List;

public interface LoomPatternContainer {
    String NBT_KEY = NBT.BANNER_PP_TAG;

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
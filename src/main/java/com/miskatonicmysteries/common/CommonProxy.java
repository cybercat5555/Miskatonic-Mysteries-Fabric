package com.miskatonicmysteries.common;

import com.miskatonicmysteries.lib.ModObjects;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer {
    @Override
    public void onInitialize() {
        ModObjects.init();
    }
}

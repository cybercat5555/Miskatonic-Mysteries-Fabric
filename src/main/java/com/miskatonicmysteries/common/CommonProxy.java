package com.miskatonicmysteries.common;

import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.lib.ModEntities;
import com.miskatonicmysteries.lib.ModObjects;
import com.miskatonicmysteries.lib.ModRecipes;
import com.miskatonicmysteries.lib.ModRegistries;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer {
    public static final CommonConfig CONFIG = ConfigManager.loadConfig(CommonConfig.class);
    @Override
    public void onInitialize() {
        ModObjects.init();
        ModEntities.init();
        ModRegistries.init();
        ModRecipes.init();
        InsanityHandler.init();
        PacketHandler.registerC2S();
    }
}

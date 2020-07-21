package com.miskatonicmysteries.common;

import com.miskatonicmysteries.client.ClientConfig;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.lib.*;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer {
    //todo, data driven stuff
    public static final CommonConfig CONFIG = ConfigManager.loadConfig(CommonConfig.class);
    @Override
    public void onInitialize() {
        ModObjects.init();
        ModRegistries.init();
        ModRecipes.init();

        PacketHandler.registerC2S();
    }
}

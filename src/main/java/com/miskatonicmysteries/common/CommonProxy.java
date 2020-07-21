package com.miskatonicmysteries.common;

import com.miskatonicmysteries.lib.*;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer {
    //todo, data driven stuff
    @Override
    public void onInitialize() {
        Constants.config = ConfigManager.loadConfig(ModConfig.class);

        ModObjects.init();
        ModRegistries.init();
        ModRecipes.init();
    }
}

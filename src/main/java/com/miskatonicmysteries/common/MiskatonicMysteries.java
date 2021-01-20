package com.miskatonicmysteries.common;

import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.lib.*;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class MiskatonicMysteries implements ModInitializer {
    public static ModConfig config;

    static {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig(); //is static init even a good idea
    }

    @Override
    public void onInitialize() {
        ModObjects.init();
        ModEntities.init();
        ModRegistries.init();
        ModRecipes.init();
        InsanityHandler.init();
        PacketHandler.registerC2S();
        ModWorld.init();
    }
}

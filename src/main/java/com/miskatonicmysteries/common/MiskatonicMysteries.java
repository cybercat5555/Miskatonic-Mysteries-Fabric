package com.miskatonicmysteries.common;

import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.lib.ModEntities;
import com.miskatonicmysteries.common.lib.ModObjects;
import com.miskatonicmysteries.common.lib.ModRecipes;
import com.miskatonicmysteries.common.lib.ModRegistries;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class MiskatonicMysteries implements ModInitializer {
    public static CommonConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(CommonConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(CommonConfig.class).getConfig();

        ModObjects.init();
        ModEntities.init();
        ModRegistries.init();
        ModRecipes.init();
        InsanityHandler.init();
        PacketHandler.registerC2S();
        //   ModWorld.init();
    }
}
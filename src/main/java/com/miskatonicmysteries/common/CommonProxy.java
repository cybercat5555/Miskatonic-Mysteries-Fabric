package com.miskatonicmysteries.common;

import com.miskatonicmysteries.client.ClientConfig;
import com.miskatonicmysteries.common.handler.DataHandler;
import com.miskatonicmysteries.common.handler.InsanityHandler;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.lib.*;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.MinecraftServer;

public class CommonProxy implements ModInitializer {
    public static final CommonConfig CONFIG = ConfigManager.loadConfig(CommonConfig.class);
    @Override
    public void onInitialize() {
        ModObjects.init();
        ModRegistries.init();
        ModRecipes.init();
        InsanityHandler.init();
        PacketHandler.registerC2S();
    }
}

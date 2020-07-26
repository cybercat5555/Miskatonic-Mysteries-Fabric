package com.miskatonicmysteries.client;

import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderChemistrySet;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.ModObjects;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ClientProxy implements ClientModInitializer {
    public static final ClientConfig CONFIG = ConfigManager.loadConfig(ClientConfig.class);
    @Override
    public void onInitializeClient() {
        FabricModelPredicateProviderRegistry.register(ModObjects.RIFLE, new Identifier("loading"), (stack, world, entity) -> ItemGun.isLoading(stack) ? 1 : 0);
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, BlockRenderChemistrySet::new);
        PacketHandler.registerS2C();
        ShaderHandler.init();
    }
}

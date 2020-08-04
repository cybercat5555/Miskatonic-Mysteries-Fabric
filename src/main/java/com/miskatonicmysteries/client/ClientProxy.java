package com.miskatonicmysteries.client;

import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderAltar;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderChemistrySet;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderOctagram;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderStatue;
import com.miskatonicmysteries.common.block.BlockAltar;
import com.miskatonicmysteries.common.block.BlockOctagram;
import com.miskatonicmysteries.common.block.BlockStatue;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.ModObjects;
import com.miskatonicmysteries.lib.ModParticles;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ClientProxy implements ClientModInitializer {
    public static final ClientConfig CONFIG = ConfigManager.loadConfig(ClientConfig.class);

    @Override
    public void onInitializeClient() {
        ModParticles.init();
        FabricModelPredicateProviderRegistry.register(ModObjects.RIFLE, new Identifier("loading"), (stack, world, entity) -> ItemGun.isLoading(stack) ? 1 : 0);
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CANDLE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.DUMMY_RESONATOR_OFF, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.DUMMY_RESONATOR_ON, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.POWERCELL, RenderLayer.getTranslucent());

        BlockAltar.ALTARS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockOctagram.OCTAGRAMS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockStatue.STATUES.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));

        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, BlockRenderChemistrySet::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.ALTAR_BLOCK_ENTITY_TYPE, BlockRenderAltar::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.OCTAGRAM_BLOCK_ENTITY_TYPE, BlockRenderOctagram::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.STATUE_BLOCK_ENTITY_TYPE, BlockRenderStatue::new);

        PacketHandler.registerS2C();
        ShaderHandler.init();

        ResourceHandler.init();
        BlockStatue.STATUES.forEach(statue -> BuiltinItemRendererRegistry.INSTANCE.register(statue.asItem(), new BlockRenderStatue.BuiltinItemStatueRenderer()));
    }
}

package com.miskatonicmysteries.client;

import com.miskatonicmysteries.client.gui.HUDHandler;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.client.render.blockentity.AltarBlockRender;
import com.miskatonicmysteries.client.render.blockentity.ChemistrySetBlockRender;
import com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender;
import com.miskatonicmysteries.client.render.blockentity.StatueBlockRender;
import com.miskatonicmysteries.client.render.entity.BoltRenderer;
import com.miskatonicmysteries.client.render.entity.HasturCultistEntityRender;
import com.miskatonicmysteries.client.render.entity.ProtagonistEntityRender;
import com.miskatonicmysteries.client.render.entity.SpellProjectileRenderer;
import com.miskatonicmysteries.client.render.entity.phantasma.PhantasmaV0Renderer;
import com.miskatonicmysteries.common.block.AltarBlock;
import com.miskatonicmysteries.common.block.OctagramBlock;
import com.miskatonicmysteries.common.block.StatueBlock;
import com.miskatonicmysteries.common.handler.networking.PacketHandler;
import com.miskatonicmysteries.common.item.GunItem;
import com.miskatonicmysteries.common.lib.MMEntities;
import com.miskatonicmysteries.common.lib.MMObjects;
import com.miskatonicmysteries.common.lib.MMParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class MiskatonicMysteriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MMParticles.init();
        FabricModelPredicateProviderRegistry.register(MMObjects.RIFLE, new Identifier("loading"), (stack, world, entity) -> GunItem.isLoading(stack) ? 1 : 0);
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.CANDLE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.RESONATOR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.POWER_CELL, RenderLayer.getTranslucent());

        AltarBlock.ALTARS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        OctagramBlock.OCTAGRAMS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        StatueBlock.STATUES.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.YELLOW_SIGN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(MMObjects.INFESTED_WHEAT_CROP, RenderLayer.getCutout());

        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, ChemistrySetBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.ALTAR_BLOCK_ENTITY_TYPE, AltarBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.OCTAGRAM_BLOCK_ENTITY_TYPE, OctagramBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(MMObjects.STATUE_BLOCK_ENTITY_TYPE, StatueBlockRender::new);

        EntityRendererRegistry.INSTANCE.register(MMEntities.PROTAGONIST, (entityRenderDispatcher, context) -> new ProtagonistEntityRender(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.HASTUR_CULTIST, (entityRenderDispatcher, context) -> new HasturCultistEntityRender(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.SPELL_PROJECTILE, (entityRenderDispatcher, context) -> new SpellProjectileRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.BOLT, (entityRenderDispatcher, context) -> new BoltRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(MMEntities.PHANTASMA, (entityRenderDispatcher, context) -> new PhantasmaV0Renderer(entityRenderDispatcher));

        PacketHandler.registerS2C();
        new ShaderHandler().init();

        ResourceHandler.init();
        StatueBlock.STATUES.forEach(statue -> BuiltinItemRendererRegistry.INSTANCE.register(statue.asItem(), new StatueBlockRender.BuiltinItemStatueRenderer()));
        HUDHandler.init();
    }
}

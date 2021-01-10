package com.miskatonicmysteries.client;

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
import com.miskatonicmysteries.common.block.AltarBlock;
import com.miskatonicmysteries.common.block.OctagramBlock;
import com.miskatonicmysteries.common.block.StatueBlock;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.item.GunItem;
import com.miskatonicmysteries.common.lib.ModEntities;
import com.miskatonicmysteries.common.lib.ModObjects;
import com.miskatonicmysteries.common.lib.ModParticles;
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
        ModParticles.init();
        FabricModelPredicateProviderRegistry.register(ModObjects.RIFLE, new Identifier("loading"), (stack, world, entity) -> GunItem.isLoading(stack) ? 1 : 0);
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CANDLE, RenderLayer.getCutout());

        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.DUMMY_RESONATOR_OFF, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.DUMMY_RESONATOR_ON, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.POWERCELL, RenderLayer.getTranslucent());

        AltarBlock.ALTARS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        OctagramBlock.OCTAGRAMS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        StatueBlock.STATUES.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.YELLOW_SIGN, RenderLayer.getCutout());

        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, ChemistrySetBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.ALTAR_BLOCK_ENTITY_TYPE, AltarBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.OCTAGRAM_BLOCK_ENTITY_TYPE, OctagramBlockRender::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.STATUE_BLOCK_ENTITY_TYPE, StatueBlockRender::new);

        EntityRendererRegistry.INSTANCE.register(ModEntities.PROTAGONIST, (entityRenderDispatcher, context) -> new ProtagonistEntityRender(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntities.HASTUR_CULTIST, (entityRenderDispatcher, context) -> new HasturCultistEntityRender(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntities.SPELL_PROJECTILE, (entityRenderDispatcher, context) -> new SpellProjectileRenderer(entityRenderDispatcher));
        EntityRendererRegistry.INSTANCE.register(ModEntities.BOLT, (entityRenderDispatcher, context) -> new BoltRenderer(entityRenderDispatcher));

        PacketHandler.registerS2C();
        ShaderHandler.init();

        ResourceHandler.init();
        StatueBlock.STATUES.forEach(statue -> BuiltinItemRendererRegistry.INSTANCE.register(statue.asItem(), new StatueBlockRender.BuiltinItemStatueRenderer()));
    }
}

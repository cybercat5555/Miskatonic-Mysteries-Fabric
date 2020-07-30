package com.miskatonicmysteries.client;

import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderAltar;
import com.miskatonicmysteries.client.render.blockentity.BlockRenderChemistrySet;
import com.miskatonicmysteries.common.block.BlockAltar;
import com.miskatonicmysteries.common.handler.PacketHandler;
import com.miskatonicmysteries.common.item.ItemGun;
import com.miskatonicmysteries.lib.Constants;
import com.miskatonicmysteries.lib.ModObjects;
import com.miskatonicmysteries.lib.ModParticles;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.EnchantingTableBlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy implements ClientModInitializer {
    private static final Map<Item, SpriteIdentifier> BOOK_SPRITES = new HashMap<>();
    public static final ClientConfig CONFIG = ConfigManager.loadConfig(ClientConfig.class);

    @Override
    public void onInitializeClient() {
        ModParticles.init();
        FabricModelPredicateProviderRegistry.register(ModObjects.RIFLE, new Identifier("loading"), (stack, world, entity) -> ItemGun.isLoading(stack) ? 1 : 0);
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CANDLE, RenderLayer.getCutout());

        BlockAltar.ALTARS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, BlockRenderChemistrySet::new);
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.ALTAR_BLOCK_ENTITY_TYPE, BlockRenderAltar::new);

        PacketHandler.registerS2C();
        ShaderHandler.init();

        ClientSpriteRegistryCallback.registerBlockAtlas((spriteAtlasTexture, registry) -> {
            registry.register(new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));
        });
        addBookTextureFor(ModObjects.NECRONOMICON, new Identifier(Constants.MOD_ID, "misc/book_necronomicon"));
    }

    public static SpriteIdentifier addBookTextureFor(Item item, Identifier texture) {

        SpriteIdentifier identifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, texture);
        BOOK_SPRITES.put(item, identifier);
        return identifier;
    }

    public static SpriteIdentifier getBookTextureFor(ItemStack stack) {
        return BOOK_SPRITES.getOrDefault(stack.getItem(), EnchantingTableBlockEntityRenderer.BOOK_TEXTURE);
    }
}

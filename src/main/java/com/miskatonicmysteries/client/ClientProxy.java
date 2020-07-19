package com.miskatonicmysteries.client;

import com.miskatonicmysteries.client.render.blockentity.BlockRenderChemistrySet;
import com.miskatonicmysteries.lib.Constants;
import com.miskatonicmysteries.lib.ModObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.Waterloggable;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClientProxy implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        /*FabricModelPredicateProviderRegistry.register(ModObjects.RIFLE, new Identifier(Constants.MOD_ID, "rifle"), new ModelPredicateProvider() {
            @Override
            public float call(ItemStack stack, ClientWorld world, LivingEntity entity) {
                return 0;
            }
        });*/
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.CHEMISTRY_SET, RenderLayer.getTranslucent());
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.CHEMISTRY_SET_BLOCK_ENTITY_TYPE, BlockRenderChemistrySet::new);
    }
}

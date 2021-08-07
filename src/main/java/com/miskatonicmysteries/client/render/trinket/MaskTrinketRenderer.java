package com.miskatonicmysteries.client.render.trinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class MaskTrinketRenderer implements TrinketRenderer {
    private final Function<EntityModelLoader, Model> modelFunction;
    private final Identifier texture;
    private Model model;

    public MaskTrinketRenderer(Function<EntityModelLoader, Model> modelFunction, Identifier texture) {
        this.modelFunction = modelFunction;
        this.texture = texture;
    }

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (model == null && MinecraftClient.getInstance().getEntityModelLoader() != null) {
            model = modelFunction.apply(MinecraftClient.getInstance().getEntityModelLoader());
        }
        if (contextModel instanceof PlayerEntityModel<?> playerEntityModel && entity instanceof AbstractClientPlayerEntity player) {
            TrinketRenderer.translateToFace(matrices, (PlayerEntityModel<AbstractClientPlayerEntity>) playerEntityModel, player, headYaw, headPitch);
        }
        matrices.translate(0, 0.25, 0.3);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(texture)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);

    }
}

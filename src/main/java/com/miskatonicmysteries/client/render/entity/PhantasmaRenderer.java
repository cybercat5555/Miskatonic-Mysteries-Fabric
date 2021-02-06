package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.common.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.feature.interfaces.Resonating;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;

public class PhantasmaRenderer extends GeoEntityRenderer<PhantasmaEntity> {

    public PhantasmaRenderer(EntityRenderDispatcher dispatcher, AnimatedGeoModel<PhantasmaEntity> model) {
        super(dispatcher, model);
        this.shadowRadius = 0;
    }

    @Override
    public Color getRenderColor(PhantasmaEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
        return new Color(1, 1, 1, (float) Math.pow(Math.min(Resonating.of(MinecraftClient.getInstance().player).map(Resonating::getResonance).orElse(0F), animatable.getResonance()), 2));
    }

    @Override
    public RenderLayer getRenderType(PhantasmaEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(textureLocation, false);
    }
}

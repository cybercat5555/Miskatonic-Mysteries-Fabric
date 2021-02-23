package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.HasturModel;
import com.miskatonicmysteries.common.entity.HasturEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;

public class HasturEntityRenderer extends GeoEntityRenderer<HasturEntity> {
    public HasturEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HasturModel(new Identifier(Constants.MOD_ID, "textures/entity/hastur/hastur.png")));
        this.shadowRadius = 0;
    }

    @Override
    public Color getRenderColor(HasturEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
        return new Color(1, 1, 1, MathHelper.clamp(1 - animatable.getPhasingProgress(), 0, 1));
    }

    @Override
    public RenderLayer getRenderType(HasturEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        RenderLayer baseLayer = RenderLayer.getEntityTranslucent(textureLocation);
        return baseLayer/* == null ? null : ShaderHandler.HASTUR_SHADER.getRenderLayer(baseLayer)*/;
    }
}

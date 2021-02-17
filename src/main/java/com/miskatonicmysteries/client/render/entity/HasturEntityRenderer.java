package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.HasturModel;
import com.miskatonicmysteries.client.render.ShaderHandler;
import com.miskatonicmysteries.common.entity.HasturEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class HasturEntityRenderer extends GeoEntityRenderer<HasturEntity> {
    public HasturEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HasturModel(new Identifier(Constants.MOD_ID, "textures/entity/hastur/hastur.png")));
        this.shadowRadius = 0;
    }

    @Override
    public RenderLayer getRenderType(HasturEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        RenderLayer baseLayer = super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
        return baseLayer == null ? null : ShaderHandler.HASTUR_SHADER.getRenderLayer(baseLayer);
    }
}

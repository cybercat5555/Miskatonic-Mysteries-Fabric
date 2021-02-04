package com.miskatonicmysteries.client.render.entity.phantasma;

import com.miskatonicmysteries.client.model.entity.phantasma.PhantasmaV0Model;
import com.miskatonicmysteries.common.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.feature.interfaces.Resonating;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;

public class PhantasmaV0Renderer extends GeoEntityRenderer<PhantasmaEntity> {
    public PhantasmaV1Renderer v1Renderer;
    public PhantasmaV2Renderer v2Renderer;

    public PhantasmaV0Renderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new PhantasmaV0Model());
        this.shadowRadius = 0;
        v1Renderer = new PhantasmaV1Renderer(dispatcher);
        v2Renderer = new PhantasmaV2Renderer(dispatcher);
    }

    @Override
    public void render(PhantasmaEntity entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn) {
        switch (entity.getVariant()) {
            case 1:
                v1Renderer.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
                break;
            case 2:
                v2Renderer.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
                break;
            default:
                super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
                break;
        }
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

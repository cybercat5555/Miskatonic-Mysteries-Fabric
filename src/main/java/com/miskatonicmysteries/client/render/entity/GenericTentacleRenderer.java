package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.TentacleModel;
import com.miskatonicmysteries.common.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

import java.awt.*;

public class GenericTentacleRenderer extends GeoEntityRenderer<TentacleEntity> {
    public GenericTentacleRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new TentacleModel(new Identifier(Constants.MOD_ID, "textures/entity/tentacle/generic.png")));
        this.shadowRadius = 0;
    }

    @Override
    public Color getRenderColor(TentacleEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
        return new Color(1, 1, 1, MathHelper.clamp(animatable.getSize(), 0, 0.5F));
    }

    @Override
    protected int getBlockLight(TentacleEntity entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public RenderLayer getRenderType(TentacleEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityTranslucent(textureLocation, false);
    }
}

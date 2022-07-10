package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.FeasterModel;
import com.miskatonicmysteries.common.feature.entity.FeasterEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class FeasterEntityRenderer extends GeoEntityRenderer<FeasterEntity> {
	private final EntityRenderDispatcher dispatcher;
	public FeasterEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new FeasterModel());
		this.shadowRadius = 1F;
		dispatcher = context.getRenderDispatcher();
	}

	@Override
	public void render(GeoModel model, FeasterEntity animatable, float partialTicks, RenderLayer type, MatrixStack matrixStackIn,
					   VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red,
					   float green, float blue, float alpha) {
		super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
					 red, green, blue, alpha);
	}

	@Override
	public RenderLayer getRenderType(FeasterEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer,
									 VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(textureLocation);
	}

	@Override
	public boolean shouldRender(FeasterEntity entity, Frustum frustum, double x, double y, double z) {
		return !entity.isInvisible();
	}
}

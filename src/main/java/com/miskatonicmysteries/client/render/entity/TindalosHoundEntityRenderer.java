package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.TindalosHoundModel;
import com.miskatonicmysteries.common.feature.entity.TentacleEntity;
import com.miskatonicmysteries.common.feature.entity.TindalosHoundEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class TindalosHoundEntityRenderer extends GeoEntityRenderer<TindalosHoundEntity> {

	public TindalosHoundEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new TindalosHoundModel());
		this.shadowRadius = 1F;
	}

	@Override
	public boolean shouldRender(TindalosHoundEntity entity, Frustum frustum, double x, double y, double z) {
		return !entity.isInvisible() && super.shouldRender(entity, frustum, x, y, z);
	}

	@Override
	public RenderLayer getRenderType(TindalosHoundEntity animatable, float partialTicks, MatrixStack stack,
		VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(textureLocation);
	}


	@Override
	public Color getRenderColor(TindalosHoundEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn) {
		int progress = animatable.getPhasingProgress();
		if (progress > 0) {
			return Color.ofRGBA(1.0F, 1.0F, 1.0F, MathHelper.clamp((progress - 100) / 100F, 0, 1.0F));
		}
		return Color.ofRGBA(1.0F, 1.0F, 1.0F, 1.0F);
	}
}

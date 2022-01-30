package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.FeasterModel;
import com.miskatonicmysteries.client.model.entity.TindalosHoundModel;
import com.miskatonicmysteries.common.feature.entity.FeasterEntity;
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
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class FeasterEntityRenderer extends GeoEntityRenderer<FeasterEntity> {

	public FeasterEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new FeasterModel());
		this.shadowRadius = 1F;
	}

	@Override
	public boolean shouldRender(FeasterEntity entity, Frustum frustum, double x, double y, double z) {
		return !entity.isInvisible() && super.shouldRender(entity, frustum, x, y, z);
	}

	@Override
	public RenderLayer getRenderType(FeasterEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(textureLocation);
	}
}

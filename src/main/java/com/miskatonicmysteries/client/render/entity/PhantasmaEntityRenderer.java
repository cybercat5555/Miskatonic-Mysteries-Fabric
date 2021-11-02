package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.common.feature.entity.PhantasmaEntity;
import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class PhantasmaEntityRenderer extends GeoEntityRenderer<PhantasmaEntity> {

	public PhantasmaEntityRenderer(EntityRendererFactory.Context context, AnimatedGeoModel<PhantasmaEntity> model) {
		super(context, model);
		this.shadowRadius = 0;
	}

	@Override
	public Color getRenderColor(PhantasmaEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer,
		VertexConsumer vertexBuilder, int packedLightIn) {
		return new Color(1, 1, 1, (float) Math.pow(
			Math.min(Resonating.of(MinecraftClient.getInstance().player).map(Resonating::getResonance).orElse(0F),
				animatable.getResonance()), 2));
	}

	@Override
	public RenderLayer getRenderType(PhantasmaEntity animatable, float partialTicks, MatrixStack stack,
		VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(textureLocation, false);
	}
}

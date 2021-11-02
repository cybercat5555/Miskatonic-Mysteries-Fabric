package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.TentacleModel;
import com.miskatonicmysteries.common.feature.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;
import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class GenericTentacleEntityRenderer extends GeoEntityRenderer<TentacleEntity> {

	public GenericTentacleEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new TentacleModel(new Identifier(Constants.MOD_ID, "textures/entity/tentacle/generic.png")));
		this.shadowRadius = 0;
	}

	@Override
	public Color getRenderColor(TentacleEntity animatable, float partialTicks, MatrixStack stack, VertexConsumerProvider renderTypeBuffer,
		VertexConsumer vertexBuilder, int packedLightIn) {
		return new Color(1, 1, 1, MathHelper.clamp(animatable.getSize(), 0, 0.5F));
	}

	@Override
	protected int getBlockLight(TentacleEntity entity, BlockPos blockPos) {
		return 15;
	}

	@Override
	public RenderLayer getRenderType(TentacleEntity animatable, float partialTicks, MatrixStack stack,
		VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
		return RenderLayer.getEntityTranslucent(textureLocation, false);
	}
}

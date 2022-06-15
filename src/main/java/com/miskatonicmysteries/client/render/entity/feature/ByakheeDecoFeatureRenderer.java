package com.miskatonicmysteries.client.render.entity.feature;

import com.miskatonicmysteries.client.model.entity.ByakheeEntityModel;
import com.miskatonicmysteries.common.feature.entity.ByakheeEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ByakheeDecoFeatureRenderer extends FeatureRenderer<ByakheeEntity, ByakheeEntityModel> {

	public static final Identifier DECO_TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/byakhee/byakhee_deco.png");

	public ByakheeDecoFeatureRenderer(FeatureRendererContext<ByakheeEntity, ByakheeEntityModel> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ByakheeEntity entity, float limbAngle,
					   float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if (entity.isDecorated()) {
			render(getContextModel(), getContextModel(), getTexture(entity), matrices, vertexConsumers, light, entity, limbAngle,
				   limbDistance, animationProgress, headYaw, headPitch, tickDelta, 1, 1, 1);
		}
	}


	@Override
	protected Identifier getTexture(ByakheeEntity entity) {
		return DECO_TEXTURE;
	}
}

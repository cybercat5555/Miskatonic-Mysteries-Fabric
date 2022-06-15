package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.entity.HarrowEntityModel;
import com.miskatonicmysteries.common.feature.entity.HarrowEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class HarrowEntityRenderer extends MobEntityRenderer<HarrowEntity, HarrowEntityModel> {

	private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/harrow/harrow.png");

	public HarrowEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new HarrowEntityModel(context.getPart(MMModels.HARROW)), 0F);
	}

	@Override
	protected void setupTransforms(HarrowEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		matrices.translate(0, -0.25F, 0);
		super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
	}

	@Override
	protected void scale(HarrowEntity entity, MatrixStack matrices, float amount) {
		float scale = 1;
		if (entity.getLifeTicks() > -1) {
			if (entity.age < 20) {
				scale = (entity.age + MinecraftClient.getInstance().getTickDelta()) / 20F;
			} else if (entity.getLifeTicks() < 20) {
				scale = (entity.getLifeTicks() - MinecraftClient.getInstance().getTickDelta()) / 20F;
			}
		}
		matrices.scale(scale, scale, scale);
	}

	@Override
	protected int getBlockLight(HarrowEntity entity, BlockPos blockPos) {
		return 15;
	}

	@Override
	public Identifier getTexture(HarrowEntity entity) {
		return TEXTURE;
	}
}

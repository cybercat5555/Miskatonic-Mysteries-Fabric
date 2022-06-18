package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.entity.GuardDogEntityModel;
import com.miskatonicmysteries.client.model.entity.HarrowEntityModel;
import com.miskatonicmysteries.common.feature.entity.GuardDogEntity;
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
public class GuardDogEntityRenderer extends MobEntityRenderer<GuardDogEntity, GuardDogEntityModel> {

	private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/guard_dog/guard_dog.png");

	public GuardDogEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new GuardDogEntityModel(context.getPart(MMModels.GUARD_DOG)), 0F);
	}

	@Override
	public Identifier getTexture(GuardDogEntity entity) {
		return TEXTURE;
	}
}

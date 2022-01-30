package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.feature.entity.FeasterEntity;
import com.miskatonicmysteries.common.feature.entity.TindalosHoundEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class FeasterModel extends AnimatedGeoModel<FeasterEntity> {

	private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/feaster/feaster.geo.json");
	private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/feaster/feaster.png");
	private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/feaster/feaster.animation.json");

	@Override
	public Identifier getModelLocation(FeasterEntity hound) {
		return MODEL;
	}

	@Override
	public Identifier getTextureLocation(FeasterEntity hound) {
		return TEXTURE;
	}

	@Override
	public Identifier getAnimationFileLocation(FeasterEntity hound) {
		return ANIMATION;
	}
}

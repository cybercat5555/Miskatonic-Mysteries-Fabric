package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.feature.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class TentacleModel extends AnimatedGeoModel<TentacleEntity> {

	private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/tentacle/tentacle.geo.json");
	private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/tentacle/tentacle.animation.json");
	private final Identifier texture;

	public TentacleModel(Identifier texture) {
		this.texture = texture;
	}

	@Override
	public Identifier getModelResource(TentacleEntity entity) {
		return MODEL;
	}

	@Override
	public Identifier getTextureResource(TentacleEntity entity) {
		return texture;
	}

	@Override
	public Identifier getAnimationResource(TentacleEntity entity) {
		return ANIMATION;
	}
}

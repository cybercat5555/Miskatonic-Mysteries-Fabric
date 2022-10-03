package com.miskatonicmysteries.client.model.entity.phantasma;

import com.miskatonicmysteries.common.feature.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.util.Identifier;

import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class AberrationModel extends AnimatedGeoModel<PhantasmaEntity> {

	private static final Identifier[] MODEL = {new Identifier(Constants.MOD_ID, "geo/aberration/aberration.geo.json")};
	private static final Identifier[] TEXTURE = {new Identifier(Constants.MOD_ID, "textures/entity/aberration/aberration.png")};
	private static final Identifier[] ANIMATION = {new Identifier(Constants.MOD_ID, "animations/aberration/aberration.animation.json")};

	@Override
	public Identifier getModelResource(PhantasmaEntity phantasmEntity) {
		return MODEL[phantasmEntity.getVariation()];
	}

	@Override
	public Identifier getTextureResource(PhantasmaEntity phantasmEntity) {
		return TEXTURE[phantasmEntity.getVariation()];
	}

	@Override
	public Identifier getAnimationResource(PhantasmaEntity phantasmEntity) {
		return ANIMATION[phantasmEntity.getVariation()];
	}
}

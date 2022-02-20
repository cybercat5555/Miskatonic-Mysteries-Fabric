package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.feature.entity.TindalosHoundEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

@Environment(EnvType.CLIENT)
public class TindalosHoundModel extends AnimatedGeoModel<TindalosHoundEntity> {

	private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/tindalos_hound/tindalos_hound.geo.json");
	private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/tindalos_hound/tindalos_hound.png");
	private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/tindalos_hound/tindalos_hound.animation.json");

	@Override
	public Identifier getModelLocation(TindalosHoundEntity hound) {
		return MODEL;
	}

	@Override
	public Identifier getTextureLocation(TindalosHoundEntity hound) {
		return TEXTURE;
	}

	@Override
	public Identifier getAnimationFileLocation(TindalosHoundEntity hound) {
		return ANIMATION;
	}
}

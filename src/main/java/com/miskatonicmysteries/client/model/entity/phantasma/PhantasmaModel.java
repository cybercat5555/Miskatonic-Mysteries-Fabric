package com.miskatonicmysteries.client.model.entity.phantasma;

import com.miskatonicmysteries.common.feature.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class PhantasmaModel extends AnimatedGeoModel<PhantasmaEntity> {

	private static final Identifier[] MODEL = {new Identifier(Constants.MOD_ID, "geo/phantasma/phantasma_0.geo.json"),
		new Identifier(Constants.MOD_ID, "geo/phantasma/phantasma_1.geo.json"),
		new Identifier(Constants.MOD_ID, "geo/phantasma/phantasma_2.geo.json")};
	private static final Identifier[] TEXTURE = {new Identifier(Constants.MOD_ID, "textures/entity/phantasma/phantasma_0.png"),
		new Identifier(Constants.MOD_ID, "textures/entity/phantasma/phantasma_1.png"),
		new Identifier(Constants.MOD_ID, "textures/entity/phantasma/phantasma_2.png")};
	private static final Identifier[] ANIMATION = {new Identifier(Constants.MOD_ID, "animations/phantasma/phantasma_0.animation.json"),
		new Identifier(Constants.MOD_ID, "animations/phantasma/phantasma_1.animation.json"),
		new Identifier(Constants.MOD_ID, "animations/phantasma/phantasma_2.animation.json")};

	@Override
	public Identifier getModelLocation(PhantasmaEntity phantasmEntity) {
		return MODEL[phantasmEntity.getVariation()];
	}

	@Override
	public Identifier getTextureLocation(PhantasmaEntity phantasmEntity) {
		return TEXTURE[phantasmEntity.getVariation()];
	}

	@Override
	public Identifier getAnimationFileLocation(PhantasmaEntity phantasmEntity) {
		return ANIMATION[phantasmEntity.getVariation()];
	}
}

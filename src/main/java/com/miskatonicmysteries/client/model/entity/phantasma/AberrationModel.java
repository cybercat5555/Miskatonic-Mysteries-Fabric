package com.miskatonicmysteries.client.model.entity.phantasma;

import com.miskatonicmysteries.common.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AberrationModel extends AnimatedGeoModel<PhantasmaEntity> {
    private static final Identifier[] MODEL = {new Identifier(Constants.MOD_ID, "geo/aberration/aberration.geo.json")};
    private static final Identifier[] TEXTURE = {new Identifier(Constants.MOD_ID, "textures/entity/aberration/aberration.png")};
    private static final Identifier[] ANIMATION = {new Identifier(Constants.MOD_ID, "animations/aberration/aberration.animation.json")};

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

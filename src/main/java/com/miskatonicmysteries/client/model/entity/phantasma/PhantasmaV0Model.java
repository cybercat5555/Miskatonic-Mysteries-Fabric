package com.miskatonicmysteries.client.model.entity.phantasma;

import com.miskatonicmysteries.common.entity.PhantasmaEntity;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PhantasmaV0Model extends AnimatedGeoModel<PhantasmaEntity> {
    private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/phantasma/phantasma_0.geo.json");
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/phantasma/phantasma_0.png");
    private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/phantasma/phantasma_0.animation.json");

    @Override
    public Identifier getModelLocation(PhantasmaEntity phantasmEntity) {
        return MODEL;
    }

    @Override
    public Identifier getTextureLocation(PhantasmaEntity phantasmEntity) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationFileLocation(PhantasmaEntity phantasmEntity) {
        return ANIMATION;
    }
}

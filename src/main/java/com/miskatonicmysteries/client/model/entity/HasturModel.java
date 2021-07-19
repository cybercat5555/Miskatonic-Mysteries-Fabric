package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HasturModel extends AnimatedGeoModel<HasturEntity> {
    private final Identifier texture;
    private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/hastur/hastur.geo.json");
    private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/hastur/hastur.animation.json");

    public HasturModel(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public Identifier getModelLocation(HasturEntity entity) {
        return MODEL;
    }

    @Override
    public Identifier getTextureLocation(HasturEntity entity) {
        return texture;
    }

    @Override
    public Identifier getAnimationFileLocation(HasturEntity entity) {
        return ANIMATION;
    }
}

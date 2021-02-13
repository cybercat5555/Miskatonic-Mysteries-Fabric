package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.entity.TentacleEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TentacleModel extends AnimatedGeoModel<TentacleEntity> {
    private final Identifier texture;
    private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/tentacle/tentacle.geo.json");
    private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/tentacle/tentacle.animation.json");

    public TentacleModel(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public Identifier getModelLocation(TentacleEntity entity) {
        return MODEL;
    }

    @Override
    public Identifier getTextureLocation(TentacleEntity entity) {
        return texture;
    }

    @Override
    public Identifier getAnimationFileLocation(TentacleEntity entity) {
        return ANIMATION;
    }
}

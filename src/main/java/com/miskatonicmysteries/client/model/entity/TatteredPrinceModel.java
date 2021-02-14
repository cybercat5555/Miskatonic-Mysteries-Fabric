package com.miskatonicmysteries.client.model.entity;

import com.miskatonicmysteries.common.entity.TatteredPrinceEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class TatteredPrinceModel extends AnimatedGeoModel<TatteredPrinceEntity> {
    private static final Identifier MODEL = new Identifier(Constants.MOD_ID, "geo/tattered_prince/tattered_prince.geo.json");
    private static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/tattered_prince/tattered_prince.png");
    private static final Identifier ANIMATION = new Identifier(Constants.MOD_ID, "animations/tattered_prince/tattered_prince.animation.json");

    @Override
    public Identifier getModelLocation(TatteredPrinceEntity entity) {
        return MODEL;
    }

    @Override
    public Identifier getTextureLocation(TatteredPrinceEntity entity) {
        return TEXTURE;
    }

    @Override
    public Identifier getAnimationFileLocation(TatteredPrinceEntity entity) {
        return ANIMATION;
    }

    @Override
    public void setLivingAnimations(TatteredPrinceEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}

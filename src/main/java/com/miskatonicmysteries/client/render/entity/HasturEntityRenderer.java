package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.HasturModel;
import com.miskatonicmysteries.common.entity.HasturEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class HasturEntityRenderer extends GeoEntityRenderer<HasturEntity> {
    public HasturEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new HasturModel(new Identifier(Constants.MOD_ID, "textures/entity/hastur/hastur.png")));
        this.shadowRadius = 0;
    }
}

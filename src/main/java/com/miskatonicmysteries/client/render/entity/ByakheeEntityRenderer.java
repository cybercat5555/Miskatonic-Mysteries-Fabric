package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.ByakheeEntityModel;
import com.miskatonicmysteries.client.render.entity.feature.ByakheeDecoFeatureRenderer;
import com.miskatonicmysteries.client.render.entity.feature.ByakheeSaddleFeatureRenderer;
import com.miskatonicmysteries.common.entity.ByakheeEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class ByakheeEntityRenderer extends MobEntityRenderer<ByakheeEntity, ByakheeEntityModel> {
    public static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/byakhee/byakhee.png");
    public static final ByakheeEntityModel MODEL = new ByakheeEntityModel();
    public ByakheeEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, MODEL, 1.5F);
        this.addFeature(new ByakheeSaddleFeatureRenderer(this));
        this.addFeature(new ByakheeDecoFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(ByakheeEntity entity) {
        return TEXTURE;
    }
}

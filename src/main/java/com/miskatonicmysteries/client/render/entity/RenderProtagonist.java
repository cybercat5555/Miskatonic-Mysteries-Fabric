package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.ModelProtagonist;
import com.miskatonicmysteries.common.entity.EntityProtagonist;
import com.miskatonicmysteries.lib.util.Constants;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class RenderProtagonist extends MobEntityRenderer<EntityProtagonist, ModelProtagonist> {
    public RenderProtagonist(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelProtagonist(), 0.5F);
    }

    @Override
    public Identifier getTexture(EntityProtagonist entity) {
        return new Identifier(Constants.MOD_ID, String.format("textures/entity/protagonist/protagonist_%d.png", entity.getVariant()));
    }
}

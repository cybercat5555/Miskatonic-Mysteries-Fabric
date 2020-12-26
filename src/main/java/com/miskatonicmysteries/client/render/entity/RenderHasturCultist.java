package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.ModelHasturCultist;
import com.miskatonicmysteries.common.entity.EntityHasturCultist;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;

public class RenderHasturCultist extends MobEntityRenderer<EntityHasturCultist, ModelHasturCultist> {
    public RenderHasturCultist(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelHasturCultist(), 0.5F);
        this.addFeature(new HeldItemFeatureRenderer<>(this));
        this.addFeature(new ArmorFeatureRenderer<>(this, new BipedEntityModel<>(0.5F), new BipedEntityModel<>(1F)));
    }

    @Override
    public Identifier getTexture(EntityHasturCultist entity) {
        return new Identifier(Constants.MOD_ID, String.format("textures/entity/hastur_cultist/%s_%d.png", entity.isAscended() ? "ascended" : "normal", entity.getVariant()));
    }
}

package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.HasturCultistEntityModel;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;

public class HasturCultistEntityRender extends MobEntityRenderer<HasturCultistEntity, HasturCultistEntityModel> {
    public HasturCultistEntityRender(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new HasturCultistEntityModel(), 0.5F);
        this.addFeature(new HeldItemFeatureRenderer<>(this));
        this.addFeature(new ArmorFeatureRenderer<>(this, new BipedEntityModel<>(0.5F), new BipedEntityModel<>(1F)));
    }

    @Override
    public Identifier getTexture(HasturCultistEntity entity) {
        return new Identifier(Constants.MOD_ID, String.format("textures/entity/hastur_cultist/%s_%d.png", entity.isAscended() ? "ascended" : "normal", entity.getVariant()));
    }
}

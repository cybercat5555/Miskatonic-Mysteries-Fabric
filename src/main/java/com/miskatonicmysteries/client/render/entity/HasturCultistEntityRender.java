package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.entity.AscendedHasturCultistEntityModel;
import com.miskatonicmysteries.client.model.entity.HasturCultistEntityModel;
import com.miskatonicmysteries.common.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HasturCultistEntityRender extends MobEntityRenderer<HasturCultistEntity, HasturCultistEntityModel> {
    public static final AscendedHasturCultistEntityModel ASCENDED_MODEL = new AscendedHasturCultistEntityModel();
    public static final HasturCultistEntityModel NORMAL_MODEL = new HasturCultistEntityModel();

    public HasturCultistEntityRender(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, NORMAL_MODEL, 0.5F);
        this.addFeature(new HeldItemFeatureRenderer<>(this));
        this.addFeature(new ArmorFeatureRenderer<>(this, new BipedEntityModel<>(0.5F), new BipedEntityModel<>(1F)));
    }

    @Override
    public void render(HasturCultistEntity mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (mobEntity.isAscended()) {
            model = ASCENDED_MODEL;
        } else {
            model = NORMAL_MODEL;
        }
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(HasturCultistEntity entity) {
        return new Identifier(Constants.MOD_ID, String.format("textures/entity/hastur_cultist/variant_%d.png", entity.getVariant()));
    }
}

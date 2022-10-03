package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.entity.AscendedHasturCultistEntityModel;
import com.miskatonicmysteries.client.model.entity.HasturCultistEntityModel;
import com.miskatonicmysteries.common.feature.entity.HasturCultistEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HasturCultistEntityRender extends MobEntityRenderer<HasturCultistEntity, HasturCultistEntityModel> {

	private final AscendedHasturCultistEntityModel ascendedModel;
	private final HasturCultistEntityModel normalModel;

	public HasturCultistEntityRender(EntityRendererFactory.Context context) {
		super(context, new HasturCultistEntityModel(context.getPart(MMModels.HASTUR_CULTIST)), 0.5F);
		this.addFeature(new HeldItemFeatureRenderer<>(this, context.getHeldItemRenderer()));
		this.addFeature(new ArmorFeatureRenderer<>(this, new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)),
												   new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR))));
		this.normalModel = model;
		this.ascendedModel = new AscendedHasturCultistEntityModel(context.getPart(MMModels.ASCENDED_HASTUR_CULTIST));
	}

	@Override
	public void render(HasturCultistEntity mobEntity, float f, float g, MatrixStack matrixStack,
					   VertexConsumerProvider vertexConsumerProvider, int i) {
		if (mobEntity.isAscended()) {
			model = ascendedModel;
		} else {
			model = normalModel;
		}
		super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(HasturCultistEntity entity) {
		return new Identifier(Constants.MOD_ID, String.format("textures/entity/hastur_cultist/variant_%d.png", entity.getVariant()));
	}
}

package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.entity.ProtagonistEntityModel;
import com.miskatonicmysteries.common.feature.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ProtagonistEntityRender extends MobEntityRenderer<ProtagonistEntity, ProtagonistEntityModel> {

	public ProtagonistEntityRender(EntityRendererFactory.Context context) {
		super(context, new ProtagonistEntityModel(context.getPart(MMModels.PROTAGONIST)), 0.5F);
		this.addFeature(new HeldItemFeatureRenderer<>(this));
		this.addFeature(new ArmorFeatureRenderer<>(this, new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE_INNER_ARMOR)),
			new ZombieEntityModel(context.getPart(EntityModelLayers.ZOMBIE_OUTER_ARMOR))));
	}

	@Override
	public Identifier getTexture(ProtagonistEntity entity) {
		return new Identifier(Constants.MOD_ID, String.format("textures/entity/protagonist/protagonist_%d.png", entity.getVariant()));
	}
}

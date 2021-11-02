package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.entity.ByakheeEntityModel;
import com.miskatonicmysteries.client.render.entity.feature.ByakheeDecoFeatureRenderer;
import com.miskatonicmysteries.client.render.entity.feature.ByakheeSaddleFeatureRenderer;
import com.miskatonicmysteries.common.feature.entity.ByakheeEntity;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ByakheeEntityRenderer extends MobEntityRenderer<ByakheeEntity, ByakheeEntityModel> {

	public static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/entity/byakhee/byakhee.png");
	public static ByakheeEntityModel byakheeEntityModel;

	public ByakheeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ByakheeEntityModel(context.getPart(MMModels.BYAKHEE)), 1.5F);
		this.addFeature(new ByakheeSaddleFeatureRenderer(this));
		this.addFeature(new ByakheeDecoFeatureRenderer(this));
		byakheeEntityModel = model;
	}

	@Override
	public Identifier getTexture(ByakheeEntity entity) {
		return TEXTURE;
	}
}

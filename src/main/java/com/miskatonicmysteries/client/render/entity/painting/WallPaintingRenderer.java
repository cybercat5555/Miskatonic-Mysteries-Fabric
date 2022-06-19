package com.miskatonicmysteries.client.render.entity.painting;

import com.miskatonicmysteries.common.feature.entity.painting.WallPaintingEntity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.entity.decoration.painting.PaintingEntity;

public class WallPaintingRenderer extends PaintingEntityRenderer {
	public WallPaintingRenderer(Context context) {
		super(context);
	}

	@Override
	public boolean shouldRender(PaintingEntity entity, Frustum frustum, double x, double y, double z) {
		return ((WallPaintingEntity) entity).noCollisionTicks == 0 && super.shouldRender(entity, frustum, x, y, z);
	}
}

package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.block.HasturObeliskModel;
import com.miskatonicmysteries.common.feature.block.blockentity.ObeliskBlockEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ObeliskBlockRender implements BlockEntityRenderer<ObeliskBlockEntity> {

	private static final SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(
		Constants.MOD_ID, "block/obelisk/hastur_obelisk"));
	private static HasturObeliskModel obelisk;

	public ObeliskBlockRender(BlockEntityRendererFactory.Context context) {
		obelisk = new HasturObeliskModel(context.getLayerModelPart(MMModels.HASTUR_OBELISK));
	}

	@Override
	public void render(ObeliskBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
					   int overlay) {
		matrices.push();
		int rotation = entity.getCachedState().get(Properties.HORIZONTAL_FACING).getId() - 2;
		matrices.translate(0.5, 1.5, 0.5);
		matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(90 * rotation));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
		VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
		obelisk.animateCloth(entity, entity.getWorld().getTime(), tickDelta);
		obelisk.render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}

package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.AltarBlockEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.LecternBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class AltarBlockRender implements BlockEntityRenderer<AltarBlockEntity> {

	private final BookModel book;

	public AltarBlockRender(BlockEntityRendererFactory.Context context) {
		this.book = new BookModel(context.getLayerModelPart(EntityModelLayers.BOOK));
	}

	@Override
	public void render(AltarBlockEntity entity, float tickDelta, MatrixStack matrixStack,
					   VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (!entity.getStack(0).isEmpty()) {
			matrixStack.push();
			matrixStack.translate(0.5D, 1.35D, 0.5D);
			float g = entity.getCachedState().get(LecternBlock.FACING).rotateYClockwise().getOpposite().asRotation();
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-g));
			matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(67.5F));
			matrixStack.translate(0.0D, -2 * Constants.BLOCK_BIT, -Constants.BLOCK_BIT);
			matrixStack.scale(0.75F, 0.75F, 0.75F);
			this.book.setPageAngles(0.0F, 0.01F, 0.99F, 1.2F);
			VertexConsumer vertexConsumer =
				ResourceHandler.getBookTextureFor(entity.getStack(0)).getVertexConsumer(vertexConsumers,
																						RenderLayer::getEntitySolid);
			this.book.renderBook(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.pop();
		}
	}

}

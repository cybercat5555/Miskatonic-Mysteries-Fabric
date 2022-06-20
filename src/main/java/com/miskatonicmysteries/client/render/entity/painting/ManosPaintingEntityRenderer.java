package com.miskatonicmysteries.client.render.entity.painting;

import com.miskatonicmysteries.common.feature.entity.painting.ManosPaintingEntity;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.PaintingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class ManosPaintingEntityRenderer extends PaintingEntityRenderer {
	private static final Identifier GUARDIAN_ACTIVE = new Identifier(Constants.MOD_ID, "textures/painting/guardian_active.png");
	private static final Identifier GUARDIAN_INACTIVE = new Identifier(Constants.MOD_ID, "textures/painting/guardian_inactive.png");

	public ManosPaintingEntityRenderer(Context context) {
		super(context);
	}

	@Override
	public void render(PaintingEntity paintingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider,
					   int i) {
		super.render(paintingEntity, f, g, matrixStack, vertexConsumerProvider, i);
		ManosPaintingEntity entity = (ManosPaintingEntity) paintingEntity;
		byte status = entity.getPaintingStatus();
		Identifier extraSprite = switch (status) {
			case 1 -> GUARDIAN_ACTIVE;
			case 2 -> GUARDIAN_INACTIVE;
			default -> null;
		};
		if (extraSprite != null) {
			matrixStack.push();
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - f));
			matrixStack.scale(0.0625f, 0.0625f, 0.0625f);
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(extraSprite));
			renderExtraLayer(matrixStack, vertexConsumer, entity, entity.getWidthPixels(), entity.getHeightPixels());
			matrixStack.pop();
		}
	}

	protected void renderExtraLayer(MatrixStack matrices, VertexConsumer vertexConsumer, ManosPaintingEntity entity, int width, int height) {
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getPositionMatrix();
		Matrix3f matrix3f = entry.getNormalMatrix();
		float f = (float)(-width) / 2.0f;
		float g = (float)(-height) / 2.0f;
		int u = width / 16;
		int v = height / 16;
		for (int w = 0; w < u; ++w) {
			for (int x = 0; x < v; ++x) {
				float y = f + (float)((w + 1) * 16);
				float z = f + (float)(w * 16);
				float aa = g + (float)((x + 1) * 16);
				float ab = g + (float)(x * 16);
				int ac = entity.getBlockX();
				int ad = MathHelper.floor(entity.getY() + (double)((aa + ab) / 2.0f / 16.0f));
				int ae = entity.getBlockZ();
				Direction direction = entity.getHorizontalFacing();
				if (direction == Direction.NORTH) {
					ac = MathHelper.floor(entity.getX() + (double)((y + z) / 2.0f / 16.0f));
				}
				if (direction == Direction.WEST) {
					ae = MathHelper.floor(entity.getZ() - (double)((y + z) / 2.0f / 16.0f));
				}
				if (direction == Direction.SOUTH) {
					ac = MathHelper.floor(entity.getX() - (double)((y + z) / 2.0f / 16.0f));
				}
				if (direction == Direction.EAST) {
					ae = MathHelper.floor(entity.getZ() + (double)((y + z) / 2.0f / 16.0f));
				}
				int af = WorldRenderer.getLightmapCoordinates(entity.world, new BlockPos(ac, ad, ae));
				float ag = 1;
				float ah = 0;
				float ai = 1;
				float aj = 0;
				this.vertex(matrix4f, matrix3f, vertexConsumer, y, ab, ah, ai, af);
				this.vertex(matrix4f, matrix3f, vertexConsumer, z, ab, ag, ai, af);
				this.vertex(matrix4f, matrix3f, vertexConsumer, z, aa, ag, aj, af);
				this.vertex(matrix4f, matrix3f, vertexConsumer, y, aa, ah, aj, af);
			}
		}
	}

	private void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, float x, float y, float u, float v, int light) {
		vertexConsumer.vertex(positionMatrix, x, y, (float) -0.5).color(255, 255, 255, 255).texture(u, v).overlay(
			OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0, 0, -1).next();
	}
}

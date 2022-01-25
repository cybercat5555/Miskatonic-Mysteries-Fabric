package com.miskatonicmysteries.client.render;

import java.util.function.Function;
import ladysnake.satin.api.util.RenderLayerHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Matrix4f;

@Environment(EnvType.CLIENT)
public class RenderHelper extends RenderLayer {

	protected static final Function<Identifier, RenderLayer> PORTAL_LAYER = Util.memoize((texture) ->
		ShaderHandler.PORTAL_CORE.getRenderLayer(RenderLayerHelper
			.copy(RenderLayer.getTranslucent(), "miskatonicmysteries:portal", (builder) -> builder
				.texture(RenderPhase.Textures.create()
					.add(texture, false, false)
					.add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).build()))));

	protected static final RenderLayer STANDARD_PORTAL = PORTAL_LAYER
		.apply(EndPortalBlockEntityRenderer.SKY_TEXTURE);

	public RenderHelper(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize,
		boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer getPortalEffect(Identifier texture) {
		return PORTAL_LAYER.apply(texture);
	}

	public static RenderLayer getTransparency() {
		return RenderLayer.getTranslucentNoCrumbling();
	}

	public static void renderTexturedPlane(float size, Sprite sprite, MatrixStack matrices, VertexConsumer buffer, int light, int overlay,
		float[] rgba) {
		Matrix4f mat = matrices.peek().getPositionMatrix();
		buffer.vertex(mat, 0, 0, size).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		buffer.vertex(mat, size, 0, size).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		buffer.vertex(mat, size, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		buffer.vertex(mat, 0, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
	}

	public static void renderCenteredTexturedPlane(float size, Sprite sprite, MatrixStack matrices, VertexConsumer buffer, int light,
		int overlay, float[] rgba, boolean twoSided) {
		Matrix4f mat = matrices.peek().getPositionMatrix();
		float halfSize = size / 2F;
		buffer.vertex(mat, -halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		buffer.vertex(mat, halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		buffer.vertex(mat, halfSize, 0, -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		buffer.vertex(mat, -halfSize, 0, -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		if (twoSided) {
			buffer.vertex(mat, -halfSize, 0, -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
				.texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
			buffer.vertex(mat, halfSize, 0, -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
				.texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
			buffer.vertex(mat, halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
				.texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
			buffer.vertex(mat, -halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3])
				.texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
		}
	}

	public static void renderPortalLayer(Identifier base, Matrix4f matrix4f, VertexConsumerProvider vertexConsumers, float sizeX,
		float sizeY, int light, int overlay, float[] rgba) {
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(getPortalEffect(base));

		vertexConsumer.vertex(matrix4f, 0, 0, sizeY).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(0, 1).light(light).overlay(overlay).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix4f, sizeX, 0, sizeY).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(1, 1).light(light).overlay(overlay).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix4f, sizeX, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(1, 0).light(light).overlay(overlay).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix4f, 0, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(0, 0).light(light).overlay(overlay).normal(0, 1, 0).next();
	}

	public static void renderModelAsPortal(VertexConsumerProvider provider, MatrixStack matrices, int light, int overlay, Model model,
		float[] rgb, float alpha) {
		model.render(matrices, provider.getBuffer(STANDARD_PORTAL), light, overlay, rgb[0], rgb[1], rgb[2], alpha);
	}
}

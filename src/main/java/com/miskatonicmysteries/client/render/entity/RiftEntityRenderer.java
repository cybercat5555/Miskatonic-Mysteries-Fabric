package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.gui.HudHandler;
import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.common.feature.entity.RiftEntity;
import com.miskatonicmysteries.common.feature.entity.SpellProjectileEntity;
import com.miskatonicmysteries.common.registry.MMParticles;
import com.miskatonicmysteries.common.registry.MMSpellMediums;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

import com.mojang.blaze3d.systems.RenderSystem;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

@Environment(EnvType.CLIENT)
public class RiftEntityRenderer extends EntityRenderer<RiftEntity> {

	public RiftEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public void render(RiftEntity entity, float yaw, float tickDelta, MatrixStack matrices,
					   VertexConsumerProvider vertexConsumers, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		matrices.push();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderHelper.STANDARD_PORTAL);
		Random random = new Random(entity.getBlockPos().asLong());
		float riftSize = MathHelper.clamp((entity.age + tickDelta) / 60F, 0, 1) * (1 + random.nextFloat() * 2);
		matrices.scale(riftSize, riftSize, riftSize);
		matrices.translate(0, 0.125, 0);
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(player.getYaw(tickDelta) + 180));
		}
		matrices.multiply(Vec3f.NEGATIVE_Z.getRadialQuaternion(random.nextFloat() * MathHelper.PI));
		matrices.translate(0, -0.5, 0);
		for (int sides = 0; sides < 2; sides++) {
			Matrix4f matrix4f = matrices.peek().getPositionMatrix();
			float[] prevPos = {0, 0};
			int segmentNumber = 4 + random.nextInt(4);
			float segmentSize = 1F / (float) segmentNumber;
			for (int i = 0; i < segmentNumber; i++) {
				prevPos = renderRiftSegment(vertexConsumer, matrix4f, prevPos[0], prevPos[1], light, OverlayTexture.DEFAULT_UV,
											entity.riftType.rgba, random, 1F, segmentSize, 1F / 8F, entity.age + tickDelta);
			}
			matrices.multiply(Vec3f.NEGATIVE_Z.getDegreesQuaternion(180));
			matrices.translate(0, -1F, 0);
		}
		matrices.pop();
	}

	private float[] renderRiftSegment(VertexConsumer vertexConsumer, Matrix4f matrix4f, float startX, float startY, int light, int overlay,
									  float[] rgba, Random random, float size, float segmentSize, float width, float progress) {
		float[] nextPos = {
			startY + segmentSize >= size ? 0 :
			width + MathHelper.sin(0.1F * progress + MathHelper.PI * random.nextFloat()) * (random.nextFloat() + 0.25F) * width * 0.5F,
			startY + segmentSize};
		vertexConsumer.vertex(matrix4f, 0, startY, 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(0, 1).light(light).overlay(overlay).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix4f, startX, startY, 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(1, 1).light(light).overlay(overlay).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix4f, nextPos[0], nextPos[1], 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(1, 0).light(light).overlay(overlay).normal(0, 1, 0).next();
		vertexConsumer.vertex(matrix4f, 0, nextPos[1], 0).color(rgba[0], rgba[1], rgba[2], rgba[3])
			.texture(0, 0).light(light).overlay(overlay).normal(0, 1, 0).next();
		return nextPos;
	}
	@Override
	public Identifier getTexture(RiftEntity entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}

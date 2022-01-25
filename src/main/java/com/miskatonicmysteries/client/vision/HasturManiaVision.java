package com.miskatonicmysteries.client.vision;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class HasturManiaVision extends VisionSequence {

	private static final int[] totalLengths = {280, 200, 400};
	private final float[] currentPos = {0.0F, 0.0F}; //not reset
	private final int version;
	private boolean moveRight, moveUp; //for dvd bounce

	public HasturManiaVision(int version) {
		this.version = version;
	}

	@Override
	public void render(MinecraftClient client, ClientPlayerEntity player, MatrixStack stack, float tickDelta) {
		ticks++;
		int width = client.getWindow().getScaledWidth();
		int height = client.getWindow().getScaledHeight();
		float backgroundProgress;
		if (ticks > 160) {
			backgroundProgress = MathHelper.clamp(1 - (ticks - 160) / 40F, 0, 1);
		} else {
			backgroundProgress = MathHelper.clamp(ticks / 80F, 0, 1);
		}
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		HasturBlessingVision.drawBackground(width, height, backgroundProgress, 0, bufferBuilder);

		switch (version) {
			case 2 -> renderDvdBounce(width, height, backgroundProgress, tessellator, bufferBuilder, stack, tickDelta);
			case 1 -> renderLines(width, height, backgroundProgress, tessellator, bufferBuilder, stack, tickDelta);
			case 0 -> renderDefaultVersion(width, height, backgroundProgress, tessellator, bufferBuilder, stack, tickDelta);
		}

		HasturBlessingVision.drawVignette(width, height, backgroundProgress, tessellator, bufferBuilder);

		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		if (ticks >= totalLengths[version] - 1) {
			VisionHandler.setVisionSequence(player, null);
			ticks = 0;
		}
	}

	@Override
	public void onStart(ClientPlayerEntity player) {
		super.onStart(player);
		moveUp = player.getRandom().nextBoolean();
		moveRight = player.getRandom().nextBoolean();
	}

	private void renderDefaultVersion(int width, int height, float backgroundProgress, Tessellator tessellator, BufferBuilder bufferBuilder,
		MatrixStack matrix, float tickDelta) {
		matrix.push();
		matrix.translate(width / 2F, height / 2F, 0.0);
		matrix.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(360.0F * (ticks + tickDelta) / 200.0F));
		matrix.translate(-width / 2F, -height / 2F, 0.0);
		HasturBlessingVision
			.drawSign(matrix.peek().getPositionMatrix(), width, height, backgroundProgress, tessellator, bufferBuilder);
		matrix.pop();
	}

	private void renderLines(int width, int height, float backgroundProgress, Tessellator tessellator, BufferBuilder bufferBuilder,
		MatrixStack matrix, float tickDelta) {
		float scale = 0.5F;
		int perRow = (int) (Math.ceil(width / (144 * scale)) + 1);
		int perColumn = (int) Math.ceil(width / (160 * scale));
		matrix.push();
		matrix.scale(scale, scale, scale);
		matrix.translate(0, -144, 0);
		for (int y = 0; y < perColumn; y++) {
			matrix.push();
			matrix.translate(y % 2 == 0 ? -144 - ticks % 144 : -288 + ticks % 144, y * 160, 0);
			for (int x = 0; x < perRow; x++) {
				HasturBlessingVision
					.drawSign(matrix.peek()
						.getPositionMatrix(), width, height, backgroundProgress, tessellator, bufferBuilder);
				matrix.translate(144, 0, 0);
			}
			matrix.pop();
		}

		matrix.pop();
	}

	private void renderDvdBounce(int width, int height, float backgroundProgress, Tessellator tessellator, BufferBuilder bufferBuilder,
		MatrixStack matrix, float tickDelta) {
		float speed = 2 + ((ticks + tickDelta) / totalLengths[2]) * 4;
		currentPos[0] += moveRight ? speed : -speed;
		currentPos[1] += moveUp ? speed : -speed;
		if (currentPos[0] >= width) {
			moveRight = false;
		} else if (currentPos[0] <= 128 * 0.3) {
			moveRight = true;
		}
		if (currentPos[1] >= height) {
			moveUp = false;
		} else if (currentPos[1] <= 128 * 0.3) {
			moveUp = true;
		}
		matrix.push();
		matrix.translate(-width / 4F, -height / 4F, 0.0);
		matrix.translate(currentPos[0], currentPos[1], 0.0);

		matrix.scale(0.3F, 0.3F, 0.3F);
		matrix.translate(64, 0, 0);
		HasturBlessingVision
			.drawSign(matrix.peek().getPositionMatrix(), width, height, backgroundProgress, tessellator, bufferBuilder);
		matrix.pop();
	}
}

package com.miskatonicmysteries.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@Environment(EnvType.CLIENT)
public class WeirdCubeParticle extends Particle {

	private static final ModelPart cube = new ModelPart(
		ImmutableList.of(new ModelPart.Cuboid(0, 0, -1.6F, -1.6F, -1.6F, 3.2F, 3.2F, 3.2F, 0, 0, 0, false, 0, 0)), ImmutableMap.of());
	private float scale;
	private float yaw, pitch, roll;
	private float yawGain, pitchGain, rollGain;

	protected WeirdCubeParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		super(clientWorld, d, e, f, g, h, i);
		this.velocityX = g;
		this.velocityY = h;
		this.velocityZ = i;
	}

	@Override
	public Particle scale(float scale) {
		this.scale = scale;
		return super.scale(scale);
	}

	@Override
	public void tick() {
		super.tick();
		yaw += yawGain;
		pitch += pitchGain;
		roll += rollGain;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		MatrixStack matrixStack = new MatrixStack();
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		int light = getBrightness(tickDelta);
		matrixStack.translate(f, g, h);
		matrixStack.scale(scale, scale, scale);
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();
		vertexConsumer = immediate.getBuffer(RenderLayer.getLeash());
		cube.yaw = yaw;
		cube.pitch = pitch;
		cube.roll = roll;
		cube.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, red, green, blue, alpha);
		immediate.draw();
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	public void move(double dx, double dy, double dz) {
		this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
		this.repositionFromBoundingBox();
	}

	public int getBrightness(float tint) {
		return 0xf000f0;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z,
									   double g, double h, double i) {
			WeirdCubeParticle cube = new WeirdCubeParticle(clientWorld, x, y, z, g, h, i);
			Random random = clientWorld.random;
			cube.scale(MathHelper.nextFloat(random, 0.5F, 1.2F));
			cube.maxAge = MathHelper.nextInt(random, 60, 100);
			cube.red = MathHelper.nextFloat(random, 0.2F, 0.4F);
			cube.green = MathHelper.nextFloat(random, 0.35F, 0.4F);
			cube.blue = MathHelper.nextFloat(random, 0.5F, 0.75F);
			cube.yawGain = MathHelper.nextFloat(random, 0.01F, 0.1F);
			cube.pitchGain = MathHelper.nextFloat(random, 0.01F, 0.1F);
			cube.rollGain = MathHelper.nextFloat(random, 0.01F, 0.1F);
			return cube;
		}
	}
}

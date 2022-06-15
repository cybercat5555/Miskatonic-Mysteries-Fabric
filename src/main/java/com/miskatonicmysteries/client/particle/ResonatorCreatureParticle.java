package com.miskatonicmysteries.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.particle.AbstractSlowingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public class ResonatorCreatureParticle extends AbstractSlowingParticle {

	private final SpriteProvider spriteProvider;
	private final Variant variant;
	private int frames;

	protected ResonatorCreatureParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h,
										double i, Variant variant, SpriteProvider spriteProvider) {
		super(clientWorld, d, e, f, g, h, i);
		this.variant = variant;
		this.spriteProvider = spriteProvider;
		this.maxAge = 100;
		this.alpha = 0;
	}

	@Override
	public void tick() {
		super.tick();
		if (age < 20) {
			alpha = age / 20F;
		} else if (age > maxAge - 20) {
			alpha = 1 - (age - (maxAge - 20)) / 20F;
			if (alpha == 0) {
				markDead();
			}
		}
		if (age % 10 == 0) {
			frames++;
			variant.movement.accept(this, frames);
			setSpriteForFrame(frames);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void move(double dx, double dy, double dz) {
		this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
		this.repositionFromBoundingBox();
	}

	public void setSpriteForFrame(int frame) {
		setSprite(spriteProvider.getSprite(variant.startFrame + 1 + (frame % variant.frames), 9));
	}

	enum Variant {
		BUG(0, 3, (particle, frames) -> {
			if (frames % 3 < 2) {
				particle.setVelocity(particle.random.nextGaussian() / 20F, particle.random.nextGaussian() / 20F,
									 particle.random.nextGaussian() / 20F);
			}
		}), JELLYFISH(3, 2, (particle, frames) -> {
			if (frames % 2 == 0) {
				particle.setVelocity(0, particle.random.nextFloat() / 10F, 0);
			}
		}), PLANKTON(5, 2, (particle, frames) -> {
		}), SKYFISH(7, 2, (particle, frames) -> {
			particle.setVelocity(particle.random.nextFloat() / 20F, 0, particle.random.nextGaussian() / 50F);
		});
		private final int startFrame;
		private final int frames;
		private final BiConsumer<ResonatorCreatureParticle, Integer> movement;

		Variant(int startFrame, int frames, BiConsumer<ResonatorCreatureParticle, Integer> movement) {
			this.startFrame = startFrame;
			this.frames = frames;
			this.movement = movement;
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x,
									   double y, double z, double g, double h, double i) {
			ResonatorCreatureParticle creatureParticle = new ResonatorCreatureParticle(clientWorld, x, y, z, g, h, i,
																					   Variant.values()[clientWorld.random
																						   .nextInt(Variant.values().length)]
				, spriteProvider);
			creatureParticle.setSpriteForFrame(0);
			return creatureParticle;
		}
	}
}

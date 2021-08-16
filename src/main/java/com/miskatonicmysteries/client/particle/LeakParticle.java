package com.miskatonicmysteries.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class LeakParticle extends SpriteBillboardParticle {

    protected LeakParticle(ClientWorld clientWorld, double x, double y, double z) {
        super(clientWorld, x, y, z);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        }
        if (onGround) {
            markDead();
        }
        if (!this.dead) {
            this.velocityY -= this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.9800000190734863D;
            this.velocityY *= 0.9800000190734863D;
            this.velocityZ *= 0.9800000190734863D;
        }
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public static class BloodFactory implements ParticleFactory<DefaultParticleType> {
        protected final SpriteProvider spriteProvider;

        public BloodFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
            LeakParticle blockLeakParticle = new LeakParticle(clientWorld, x, y, z);
            blockLeakParticle.velocityX += g;
            blockLeakParticle.velocityY += h;
            blockLeakParticle.velocityZ += i;
            blockLeakParticle.gravityStrength = 0.01F;
            blockLeakParticle.setColor(0.65F, 0, 0);
            blockLeakParticle.setSprite(this.spriteProvider);
            return blockLeakParticle;
        }
    }
}

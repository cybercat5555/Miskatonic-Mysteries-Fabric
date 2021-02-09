package com.miskatonicmysteries.client.particle;

import com.miskatonicmysteries.common.registry.MMParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;

public class ShrinkingMagicParticle extends SpriteBillboardParticle {

    protected ShrinkingMagicParticle(ClientWorld clientWorld, double x, double y, double z, float r, float g, float b) {
        super(clientWorld, x, y, z, 0, 0, 0);
        colorAlpha = 0.85F;
        colorRed = r;
        colorGreen = g;
        colorBlue = b;
        velocityX = 0;
        velocityY = 0;
        velocityZ = 0;
        scale = 0.5F;
        maxAge = 20;
    }

    public ParticleTextureSheet getType() {
        return MMParticles.ParticleTextureSheets.GLOWING;
    }

    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public void tick() {
        float lifeRatio = (float) this.age / (float) this.maxAge;
        this.scale = scale - (lifeRatio * scale);
        super.tick();
    }

    public int getColorMultiplier(float tint) {
        float f = ((float) this.age + tint) / (float) this.maxAge;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getColorMultiplier(tint);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }

        return j | k << 16;
    }

    @Override
    public void setSprite(SpriteProvider spriteProvider) {
        super.setSprite(spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double r, double g, double b) {
            ShrinkingMagicParticle particle = new ShrinkingMagicParticle(clientWorld, x, y, z, (float) r, (float) g, (float) b);
            particle.setSpriteForAge(this.spriteProvider);
            return particle;
        }
    }
}

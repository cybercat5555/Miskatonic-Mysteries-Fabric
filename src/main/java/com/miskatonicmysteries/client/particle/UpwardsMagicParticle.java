package com.miskatonicmysteries.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import static net.minecraft.client.texture.SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE;

public class UpwardsMagicParticle extends AbstractSlowingParticle {

    protected UpwardsMagicParticle(ClientWorld clientWorld, double x, double y, double z, float r, float g, float b) {
        super(clientWorld, x, y, z, 0, Math.max(0.01F, clientWorld.random.nextFloat() / 50), 0);
        colorAlpha = 0.85F;
        colorRed = r;
        colorGreen = g;
        colorBlue = b;
        maxAge = 60 + clientWorld.random.nextInt(20);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
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

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        MinecraftClient.getInstance().getTextureManager().getTexture(PARTICLE_ATLAS_TEXTURE).setFilter(true, false);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        RenderSystem.disableLighting();
        super.buildGeometry(vertexConsumer, camera, tickDelta);
        //  MinecraftClient.getInstance().getTextureManager().getTexture(PARTICLE_ATLAS_TEXTURE).setFilter(false, false);
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
            UpwardsMagicParticle particle = new UpwardsMagicParticle(clientWorld, x, y, z, (float) r, (float) g, (float) b);
            particle.scale(0.75F + clientWorld.random.nextFloat() / 5F);
            particle.setSpriteForAge(this.spriteProvider);
            return particle;
        }
    }
}

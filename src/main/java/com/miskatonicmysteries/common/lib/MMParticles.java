package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.client.particle.AmbientMagicParticle;
import com.miskatonicmysteries.client.particle.CandleFlameParticle;
import com.miskatonicmysteries.client.particle.LeakParticle;
import com.miskatonicmysteries.client.particle.ShrinkingMagicParticle;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class MMParticles {
    public static final DefaultParticleType FLAME = FabricParticleTypes.simple(true);
    public static final DefaultParticleType AMBIENT = FabricParticleTypes.simple(true);
    public static final DefaultParticleType SHRINKING_MAGIC = FabricParticleTypes.simple(true);
    public static final DefaultParticleType DRIPPING_BLOOD = FabricParticleTypes.simple(true);

    public static void init() {
        RegistryUtil.register(Registry.PARTICLE_TYPE, "flame", FLAME);
        ParticleFactoryRegistry.getInstance().register(FLAME, CandleFlameParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "magic_upwards", AMBIENT);
        ParticleFactoryRegistry.getInstance().register(AMBIENT, AmbientMagicParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "magic_shrinking", SHRINKING_MAGIC);
        ParticleFactoryRegistry.getInstance().register(SHRINKING_MAGIC, ShrinkingMagicParticle.Factory::new);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "blood", DRIPPING_BLOOD);
        ParticleFactoryRegistry.getInstance().register(DRIPPING_BLOOD, LeakParticle.BloodFactory::new);
    }

    public static void spawnCandleParticle(World world, double x, double y, double z, float size, boolean alwaysSpawn) {
        if (alwaysSpawn || world.random.nextBoolean()) {
            world.addParticle(MMParticles.FLAME, x, y, z, size + world.random.nextGaussian() / 20F, 0, 0);
        }
    }

    public static class ParticleTextureSheets {
        public static final ParticleTextureSheet GLOWING = new ParticleTextureSheet() {
            @Override
            public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                RenderSystem.disableLighting();

                textureManager.bindTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
                AbstractTexture tex = textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
                tex.setFilter(true, false);
                bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
            }

            @Override
            public void draw(Tessellator tessellator) {
                AbstractTexture tex = MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
                tex.setFilter(false, false);
                tessellator.draw();
                RenderSystem.defaultAlphaFunc();
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
            }

            @Override
            public String toString() {
                return Constants.MOD_ID + ":glowing";
            }
        };
    }
}
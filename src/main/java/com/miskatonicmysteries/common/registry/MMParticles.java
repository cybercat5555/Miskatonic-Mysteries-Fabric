package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
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
    public static final DefaultParticleType AMBIENT_MAGIC = FabricParticleTypes.simple(true);
    public static final DefaultParticleType SHRINKING_MAGIC = FabricParticleTypes.simple(true);
    public static final DefaultParticleType DRIPPING_BLOOD = FabricParticleTypes.simple(true);
    public static final DefaultParticleType RESONATOR_CREATURE = FabricParticleTypes.simple(true);

    public static void init() {
        RegistryUtil.register(Registry.PARTICLE_TYPE, "flame", FLAME);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "ambient", AMBIENT);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "ambient_magic", AMBIENT_MAGIC);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "magic_shrinking", SHRINKING_MAGIC);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "blood", DRIPPING_BLOOD);
        RegistryUtil.register(Registry.PARTICLE_TYPE, "resonator_creature", RESONATOR_CREATURE);
    }


    @Environment(EnvType.CLIENT)
    public static void spawnCandleParticle(World world, double x, double y, double z, float size, boolean alwaysSpawn) {
        if (alwaysSpawn || world.random.nextBoolean()) {
            world.addParticle(MMParticles.FLAME, x, y, z, size + world.random.nextGaussian() / 20F, 0, 0);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ParticleTextureSheets {
        public static final ParticleTextureSheet GLOWING = new ParticleTextureSheet() {
            @Override
            public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                textureManager.bindTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
                AbstractTexture tex = textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
                tex.setFilter(true, false);
                bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
            }

            @Override
            public void draw(Tessellator tessellator) {
                AbstractTexture tex = MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
                tex.setFilter(false, false);
                tessellator.draw();
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
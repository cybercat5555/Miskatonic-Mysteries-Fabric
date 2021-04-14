package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.common.util.Constants;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.satin.api.util.RenderLayerHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Environment(EnvType.CLIENT)
public class RenderHelper extends RenderLayer {
    public static final List<RenderLayer> PORTAL_LAYERS = IntStream.range(1, 17).mapToObj(RenderLayer::getEndPortal).collect(Collectors.toList());
    public static final RenderPhase.Transparency AURA_TRANSPARENCY = new RenderPhase.Transparency(Constants.MOD_ID + ":aura_transparency", () -> {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
        RenderSystem.disableLighting();
    }, () -> {
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    public static final MultiPhaseParameters TRANSPARENCY_PARAMS = RenderLayer.MultiPhaseParameters.builder()
            .shadeModel(new RenderPhase.ShadeModel(false))
            .texture(BLOCK_ATLAS_TEXTURE)
            .diffuseLighting(new RenderPhase.DiffuseLighting(true))
            .transparency(new RenderPhase.Transparency(Constants.MOD_ID + ":translucency", () -> {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);

            }, () -> {
                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            }))
            .lightmap(new RenderPhase.Lightmap(true))
            .build(true);

    public static final RenderLayer.MultiPhaseParameters AURA_PARAMS = RenderLayer.MultiPhaseParameters.builder()
            .shadeModel(new RenderPhase.ShadeModel(false))
            .texture(BLOCK_ATLAS_TEXTURE)
            .diffuseLighting(new RenderPhase.DiffuseLighting(true))
            .transparency(AURA_TRANSPARENCY)
            .alpha(RenderPhase.ONE_TENTH_ALPHA)
            .lightmap(new RenderPhase.Lightmap(true))
            .build(true);

    public static final RenderLayer AURA_LAYER = RenderLayer.of(Constants.MOD_ID + ":aura_layer", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, true, true, AURA_PARAMS);
    public static final RenderLayer TRANSPARENCY_LAYER = RenderLayer.of(Constants.MOD_ID + ":transparent", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, true, true, TRANSPARENCY_PARAMS);
    public static final RenderLayer BOLT_LAYER = RenderLayer.of(Constants.MOD_ID + ":bolt", VertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256, false, true, RenderLayerHelper.copyPhaseParameters(getLightning(), builder -> builder.transparency(AURA_TRANSPARENCY)));

    public RenderHelper(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getAuraGlowLayer() {
        return AURA_LAYER;
    }

    public static RenderLayer getTransparency() {
        return TRANSPARENCY_LAYER;
    }

    public static RenderLayer getBoltLayer() {
        return BOLT_LAYER;
    }

    public static RenderLayer getEnergyTentacleLayer(Identifier texture) {
        RenderPhase.Transparency TRANSPARENCY = new RenderPhase.Transparency("translucent_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.disableLighting();
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
        RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSPARENCY).diffuseLighting(ENABLE_DIFFUSE_LIGHTING).alpha(ONE_TENTH_ALPHA).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return of(Constants.MOD_ID + ":energy_tentacle", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, 7, 256, true, true, multiPhaseParameters);
    }

    public static void renderTexturedPlane(float size, Sprite sprite, MatrixStack matrices, VertexConsumer buffer, int light, int overlay, float[] rgba) {
        Matrix4f mat = matrices.peek().getModel();
        buffer.vertex(mat, 0, 0, size).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, size, 0, size).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, size, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 0, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(1, 1, 1).next();
    }

    public static void renderFullPortal(Camera camera, World world, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers, float size, float[] rgb) {
        matrices.push();
        double distance = pos.getSquaredDistance(camera.getPos(), true);
        int renderDepth = getDepthFromDistance(distance);
        Matrix4f matrix4f = matrices.peek().getModel();
        for (int i = 0; i < renderDepth; ++i) {
            renderPortalLayer(i, world, matrix4f, vertexConsumers, size, rgb);
        }
        matrices.pop();
    }

    public static void renderPortalLayer(int layer, World world, Matrix4f matrix4f, VertexConsumerProvider vertexConsumers, float size, float[] rgb) {
        renderPortalLayer(layer, world, matrix4f, vertexConsumers, size, size,rgb);
    }

    public static void renderPortalLayer(int layer, World world, Matrix4f matrix4f, VertexConsumerProvider vertexConsumers, float sizeX, float sizeY, float[] rgb) {
        float colorFactor = 2f / (18 - layer);
        float r = (world.random.nextFloat() * 0.5f + rgb[0]) * colorFactor;
        float g = (world.random.nextFloat() * 0.5f + rgb[1]) * colorFactor;
        float b = (world.random.nextFloat() * 0.5f + rgb[2]) * colorFactor;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(PORTAL_LAYERS.get(layer));
        vertexConsumer.vertex(matrix4f, 0, 0, sizeY).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, sizeX, 0, sizeY).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, sizeX, 0, 0).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, 0, 0, 0).color(r, g, b, rgb[3]).next();
    }

    public static void renderModelAsPortal(VertexConsumerProvider provider, MatrixStack matrices, int light, int overlay, Model model, float[] rgb, float alpha, Random random, int layer) {
        model.render(matrices, provider.getBuffer(PORTAL_LAYERS.get(layer)), light, overlay, rgb[0], rgb[1], rgb[2], alpha);
    }

    public static int getDepthFromDistance(double distance) {
        if (distance > 36864) {
            return 1;
        } else if (distance > 25600) {
            return 3;
        } else if (distance > 16384) {
            return 5;
        } else if (distance > 9216) {
            return 7;
        } else if (distance > 4096) {
            return 9;
        } else if (distance > 1024) {
            return 11;
        } else if (distance > 576) {
            return 13;
        } else {
            return distance > 256 ? 14 : 15;
        }
    }
}

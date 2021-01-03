package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RenderHelper extends RenderLayer {
    private static final List<RenderLayer> PORTAL_LAYERS = IntStream.range(1, 17).mapToObj(RenderLayer::getEndPortal).collect(Collectors.toList());

    public RenderHelper(String name, VertexFormat vertexFormat, int drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getOctagramLayer() {
        RenderLayer.MultiPhaseParameters param = RenderLayer.MultiPhaseParameters.builder()
                .shadeModel(new RenderPhase.ShadeModel(true))
                .texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false, true))
                .diffuseLighting(new RenderPhase.DiffuseLighting(true))
                .alpha(new RenderPhase.Alpha(0.04F))
                .lightmap(new RenderPhase.Lightmap(true))
                .build(true);
        return RenderLayer.of(Constants.MOD_ID + ":octagram_layer", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, true, true, param);
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
        float colorFactor = 2f / (18 - layer);
        float r = (world.random.nextFloat() * 0.5f + rgb[0]) * colorFactor;
        float g = (world.random.nextFloat() * 0.5f + rgb[1]) * colorFactor;
        float b = (world.random.nextFloat() * 0.5f + rgb[2]) * colorFactor;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(PORTAL_LAYERS.get(layer));
        vertexConsumer.vertex(matrix4f, 0, 0, size).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, size, 0, size).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, size, 0, 0).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, 0, 0, 0).color(r, g, b, rgb[3]).next();
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

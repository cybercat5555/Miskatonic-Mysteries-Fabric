package com.miskatonicmysteries.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.option.CloudRenderMode;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class RenderHelper extends RenderLayer {

    public RenderHelper(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer getTransparency() {
        return RenderLayer.getTranslucentNoCrumbling();
    }

    public static void renderTexturedPlane(float size, Sprite sprite, MatrixStack matrices, VertexConsumer buffer, int light, int overlay, float[] rgba) {
        Matrix4f mat = matrices.peek().getModel();
        buffer.vertex(mat, 0, 0, size).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        buffer.vertex(mat, size, 0, size).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        buffer.vertex(mat, size, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        buffer.vertex(mat, 0, 0, 0).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
    }

    public static void renderCenteredTexturedPlane(float size, Sprite sprite, MatrixStack matrices, VertexConsumer buffer, int light, int overlay, float[] rgba, boolean twoSided) {
        Matrix4f mat = matrices.peek().getModel();
        float halfSize = size / 2F;
        buffer.vertex(mat, -halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        buffer.vertex(mat, halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        buffer.vertex(mat, halfSize, 0, -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        buffer.vertex(mat,  -halfSize, 0,  -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        if (twoSided){
            buffer.vertex(mat,  -halfSize, 0,  -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
            buffer.vertex(mat, halfSize, 0, -halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(0, 1, 0).next();
            buffer.vertex(mat, halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
            buffer.vertex(mat, -halfSize, 0, halfSize).color(rgba[0], rgba[1], rgba[2], rgba[3]).texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(0, 1, 0).next();
        }
    }

    public static void renderPortalLayer(World world, Matrix4f matrix4f, VertexConsumerProvider vertexConsumers, float sizeX, float sizeY, float[] rgb) {
        float r = (world.random.nextFloat() * 0.5f + rgb[0]);
        float g = (world.random.nextFloat() * 0.5f + rgb[1]);
        float b = (world.random.nextFloat() * 0.5f + rgb[2]);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEndPortal());
        vertexConsumer.vertex(matrix4f, 0, 0, sizeY).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, sizeX, 0, sizeY).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, sizeX, 0, 0).color(r, g, b, rgb[3]).next();
        vertexConsumer.vertex(matrix4f, 0, 0, 0).color(r, g, b, rgb[3]).next();
    }

    public static void renderModelAsPortal(VertexConsumerProvider provider, MatrixStack matrices, int light, int overlay, Model model, float[] rgb, float alpha) {
        model.render(matrices, provider.getBuffer(RenderLayer.getEndPortal()), light, overlay, rgb[0], rgb[1], rgb[2], alpha);
    }
}

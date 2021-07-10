package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.common.entity.BoltEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import java.util.Random;

public class BoltEntityRenderer extends EntityRenderer<BoltEntity> {
    public BoltEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    public void render(BoltEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        int divisionAngle = 50;
        int segmentNumber = 7;
        Random random = new Random(entity.seed);
        float sizeMod = Math.min((entity.age + tickDelta) / 2, 1);
        float currentLength = entity.getLength() * sizeMod;
        float maxDistancePerSegment = entity.getLength() / (float) segmentNumber;

        int targetNumber = (int) (segmentNumber * sizeMod);
        float size = 0.02F;
        Vec3d rgb = Vec3d.unpackRgb(entity.getColor());
        float alpha = entity.age < 3 ? 0.3F : Math.max(0.3F * (1 - (entity.age - 3 + tickDelta) / 3), 0);

        float[] segmentLengths = new float[segmentNumber];
        for (int segment = 0; segment < segmentLengths.length; segment++) {
            if (segment == targetNumber) {
                segmentLengths[segment] = currentLength - maxDistancePerSegment * segment;
            } else {
                segmentLengths[segment] = maxDistancePerSegment;
            }
        }
        float[] offsetsY = new float[segmentNumber];
        float[] offsetsX = new float[segmentNumber];
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderHelper.getBoltLayer());
        Matrix4f matrix4f = matrices.peek().getModel();

        matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(entity.getYaw(tickDelta)));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(entity.getPitch(tickDelta)));
        matrices.translate(0, 0, 0.1);
        for (int branches = 0; branches < 5; branches++) {
            float lastOffsetY = 0.0F;
            float lastOffsetX = 0.0F;
            for (int i = 0; i < segmentNumber; i++) {
                offsetsY[i] = lastOffsetY;
                offsetsX[i] = lastOffsetX;
                lastOffsetY += Math.sin(Math.toRadians(random.nextInt(divisionAngle) - divisionAngle / 2F));
                lastOffsetX += Math.sin(Math.toRadians(random.nextInt(divisionAngle) - divisionAngle / 2F));
            }

            for (int segment = 0; segment < segmentNumber; segment++) {
                float startY = offsetsY[segment];
                float startX = offsetsX[segment];

                for (int depth = 1; depth < 4; depth++) {
                    float depthY = (size / 2F) + (float) depth * size;
                    float depthZ = (size / 2F) + (float) depth * size;

                    float endY = ((segment == segmentNumber - 1) ? startY : offsetsY[segment + 1]);
                    float endX = ((segment == segmentNumber - 1) ? startX : offsetsX[segment + 1]);
                    if (segment <= targetNumber) {
                        drawQuad(matrix4f, vertexConsumer, startY, startX, segment, endY, endX, (float) rgb.x, (float) rgb.y, (float) rgb.z, alpha, depthY, depthZ, false, false, true, false, maxDistancePerSegment, segmentLengths[segment]);
                        drawQuad(matrix4f, vertexConsumer, startY, startX, segment, endY, endX, (float) rgb.x, (float) rgb.y, (float) rgb.z, alpha, depthY, depthZ, true, false, true, true, maxDistancePerSegment, segmentLengths[segment]);
                        drawQuad(matrix4f, vertexConsumer, startY, startX, segment, endY, endX, (float) rgb.x, (float) rgb.y, (float) rgb.z, alpha, depthY, depthZ, true, true, false, true, maxDistancePerSegment, segmentLengths[segment]);
                        drawQuad(matrix4f, vertexConsumer, startY, startX, segment, endY, endX, (float) rgb.x, (float) rgb.y, (float) rgb.z, alpha, depthY, depthZ, false, true, false, false, maxDistancePerSegment, segmentLengths[segment]);
                    }
                }
            }
        }
    }

    private static void drawQuad(Matrix4f matrix4f, VertexConsumer vertexConsumer, float startY, float startX, int segmentIndex, float endY, float endX, float red, float green, float blue, float alpha, float firstOffset, float secondOffset, boolean negativeOffset, boolean bl2, boolean bl3, boolean bl4, float segmentLength, float segmentLengthAdded) {
        vertexConsumer.vertex(matrix4f, startX + (bl2 ? secondOffset : -secondOffset), startY + (negativeOffset ? secondOffset : -secondOffset), (segmentIndex * segmentLength)).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix4f, endX + (bl2 ? firstOffset : -firstOffset), endY + (negativeOffset ? firstOffset : -firstOffset), (segmentIndex) * segmentLength + segmentLengthAdded).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix4f, endX + (bl4 ? firstOffset : -firstOffset), endY + (bl3 ? firstOffset : -firstOffset), ((segmentIndex) * segmentLength + segmentLengthAdded)).color(red, green, blue, alpha).next();
        vertexConsumer.vertex(matrix4f, startX + (bl4 ? secondOffset : -secondOffset), startY + (bl3 ? secondOffset : -secondOffset), (segmentIndex * segmentLength)).color(red, green, blue, alpha).next();
    }

    @Override
    public Identifier getTexture(BoltEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}

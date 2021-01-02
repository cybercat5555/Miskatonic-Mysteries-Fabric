package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.render.RenderLayerHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OctagramBlockRender extends BlockEntityRenderer<OctagramBlockEntity> {
    public static final RenderLayer OCTAGRAM_RENDER_LAYER = RenderLayerHelper.getOctagramLayer();
    private static List<RenderLayer> PORTAL_LAYERS = IntStream.range(0, 16).mapToObj((i) -> RenderLayerHelper.getPortalLayer(i + 1)).collect(Collectors.toList());
    private static final Random RANDOM = new Random(31100L);

    public OctagramBlockRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        PORTAL_LAYERS = IntStream.range(0, 16).mapToObj((i) -> RenderLayerHelper.getPortalLayer(i + 1)).collect(Collectors.toList());
        Sprite sprite = ResourceHandler.getOctagramTextureFor(entity).getSprite();

        VertexConsumer buffer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(OCTAGRAM_RENDER_LAYER));
        matrixStack.push();
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(HorizontalFacingBlock.FACING);
        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation()));
        matrixStack.translate(-1.5, 0.001, -1.5);
        byte overrideRender = entity.currentRite != null ? entity.currentRite.renderRite(entity, tickDelta, matrixStack, vertexConsumers, light, overlay) : 3;
        if ((overrideRender & 1) == 1) renderOctagram(entity, sprite, matrixStack, buffer, light);
        matrixStack.pop();
        if ((overrideRender >> 1 & 1) == 1) renderItems(entity, vertexConsumers, matrixStack, light);
        matrixStack.pop();
    }

    public static void renderOctagram(OctagramBlockEntity entity, Sprite sprite, MatrixStack matrices, VertexConsumer buffer, int light) {
        Matrix4f mat = matrices.peek().getModel();
        buffer.vertex(mat, 0, 0, 3).color(1F, 1F, 1F, 1F).texture(sprite.getMinU(), sprite.getMaxV()).light(light).next();
        buffer.vertex(mat, 3, 0, 3).color(1F, 1F, 1F, 1F).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).next();
        buffer.vertex(mat, 3, 0, 0).color(1F, 1F, 1F, 1F).texture(sprite.getMaxU(), sprite.getMinV()).light(light).next();
        buffer.vertex(mat, 0, 0, 0).color(1F, 1F, 1F, 1F).texture(sprite.getMinU(), sprite.getMinV()).light(light).next();
    }

    public static void renderItems(OctagramBlockEntity entity, VertexConsumerProvider vertexConsumers, MatrixStack matrixStack, int light) {
        matrixStack.translate(0.5F, 0, 0.5F);

        for (int i = 0; i < entity.size(); i++) {
            matrixStack.push();
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(0.125F * i * 360F));
            matrixStack.translate(0, 0, 1.1);
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(i), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers);
            matrixStack.pop();
        }
    }
    private void renderPortal(OctagramBlockEntity entity, BlockPos pos, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        double distance = pos.getSquaredDistance(dispatcher.camera.getPos(), true);
        int renderDepth = getDepthFromDistance(distance);
        Matrix4f matrix4f = matrices.peek().getModel();
        for (int i = 0; i < renderDepth; ++i) {
            float colorFactor = 2f / (18 - i);
            float r = (entity.getWorld().random.nextFloat() * 0.5f + 0.1f) * colorFactor;
            float g = (entity.getWorld().random.nextFloat() * 0.5f + 0.4f) * colorFactor;
            float b = (entity.getWorld().random.nextFloat() * 0.5f + 0.5f) * colorFactor;
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(PORTAL_LAYERS.get(i));
            vertexConsumer.vertex(matrix4f, 0, 0, 3).color(r, g, b, 1).next();
            vertexConsumer.vertex(matrix4f, 3, 0, 3).color(r, g, b, 1).next();
            vertexConsumer.vertex(matrix4f, 3, 0, 0).color(r, g, b, 1).next();
            vertexConsumer.vertex(matrix4f, 0, 0, 0).color(r, g, b, 1).next();
        }
    }

    private int getDepthFromDistance(double distance) {
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

package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.render.RenderHelper;
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
import net.minecraft.util.math.Direction;

public class OctagramBlockRender extends BlockEntityRenderer<OctagramBlockEntity> {
    public static final RenderLayer OCTAGRAM_RENDER_LAYER = RenderHelper.getOctagramLayer();

    public OctagramBlockRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Sprite sprite = ResourceHandler.getOctagramTextureFor(entity).getSprite();
        VertexConsumer buffer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getCutout()));
        matrixStack.push();
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(HorizontalFacingBlock.FACING);
        byte overrideRender = entity.currentRite != null ? entity.currentRite.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, dispatcher) : 3;
        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation()));
        matrixStack.translate(-1.5, 0.001, -1.5);

        if ((overrideRender & 1) == 1) {
            RenderHelper.renderTexturedPlane(3, sprite, matrixStack, buffer, light, overlay, new float[]{1, 1, 1, 1});
        }
        if (entity.currentRite != null) {
            entity.currentRite.renderRite(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, dispatcher);
        }
        matrixStack.pop();
        if ((overrideRender >> 1 & 1) == 1) {
            renderItems(entity, vertexConsumers, matrixStack, light);
        }
        matrixStack.pop();
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
}

package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;

public class StatueBlockRender extends BlockEntityRenderer<StatueBlockEntity> {
    public StatueBlockRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(StatueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        int rotation = entity.getWorld().getBlockState(entity.getPos()).get(Properties.ROTATION);
        matrices.translate(0.5, 1.5, 0.5);
        matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion((0.125F / 2F) * rotation * 360F));
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
        VertexConsumer vertexConsumer = ResourceHandler.getStatueTextureFor(entity).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        ResourceHandler.getStatueModelFor(entity).render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }

    public static class BuiltinItemStatueRenderer implements BuiltinItemRenderer {
        @Override
        public void render(ItemStack itemStack, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int i1) {
            Block block = Block.getBlockFromItem(itemStack.getItem());
            if (block instanceof StatueBlock) {
                matrixStack.push();
                matrixStack.translate(0.5, 1.4, 0.5);
                matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(270));
                matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
                VertexConsumer vertexConsumer = ResourceHandler.STATUE_SPRITES.get(block).getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
                ResourceHandler.STATUE_MODELS.get(((StatueBlock) block).getAffiliation(true)).render(matrixStack, vertexConsumer, i, i1, 1.0F, 1.0F, 1.0F, 1.0F);
                matrixStack.pop();
            }
        }
    }
}

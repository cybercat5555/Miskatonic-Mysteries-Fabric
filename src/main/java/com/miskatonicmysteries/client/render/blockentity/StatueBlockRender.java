package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Vec3f;

public class StatueBlockRender implements BlockEntityRenderer<StatueBlockEntity> {
    public StatueBlockRender(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(StatueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        int rotation = entity.getWorld().getBlockState(entity.getPos()).get(Properties.ROTATION);
        matrices.translate(0.5, 1.5, 0.5);
        matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion((0.125F / 2F) * rotation * 360F));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        VertexConsumer vertexConsumer = ResourceHandler.getStatueTextureFor(entity).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        ResourceHandler.getStatueModelFor(entity).render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }

    public static class BuiltinItemStatueRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
        @Override
        public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block instanceof StatueBlock) {
                matrices.push();
                matrices.translate(0.5, 1.4, 0.5);
                matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(270));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                VertexConsumer vertexConsumer = ResourceHandler.STATUE_SPRITES.get(block).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
                ResourceHandler.STATUE_MODELS.get(((StatueBlock) block).getAffiliation(true)).render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.pop();
            }
        }
    }
}

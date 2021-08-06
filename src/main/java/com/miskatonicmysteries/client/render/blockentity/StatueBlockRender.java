package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.model.MMModels;
import com.miskatonicmysteries.client.model.block.StatueModel;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.StatueBlockEntity;
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

import java.util.HashMap;
import java.util.Map;

public class StatueBlockRender implements BlockEntityRenderer<StatueBlockEntity> {
    public static final Map<Affiliation, StatueModel> MODELS = new HashMap<>();
    public StatueBlockRender(BlockEntityRendererFactory.Context context) {
        MMModels.STATUE_MODELS.forEach(((affiliation, modelFunction) -> MODELS.put(affiliation, modelFunction.apply(context))));
    }

    @Override
    public void render(StatueBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        int rotation = entity.getCachedState().get(Properties.ROTATION);
        matrices.translate(0.5, 1.5, 0.5);
        matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion((0.125F / 2F) * rotation * 360F));
        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
        VertexConsumer vertexConsumer = ResourceHandler.getStatueTextureFor(entity).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        MODELS.get(entity.getAffiliation(false)).render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrices.pop();
    }

    public static class BuiltinItemStatueRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

        @Override
        public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
            Block block = Block.getBlockFromItem(stack.getItem());
            if (block instanceof StatueBlock statue) {
                matrices.push();
                matrices.translate(0.5, 1.4, 0.5);
                matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(270));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180));
                VertexConsumer vertexConsumer = ResourceHandler.STATUE_SPRITES.get(block).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
                MODELS.get(statue.getAffiliation(false)).render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.pop();
            }
        }
    }
}

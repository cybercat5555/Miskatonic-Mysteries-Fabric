package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.common.block.blockentity.ChemistrySetBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class ChemistrySetBlockRender implements BlockEntityRenderer<ChemistrySetBlockEntity> {
    public ChemistrySetBlockRender(BlockEntityRendererFactory.Context context) {
        //no-op
    }

    @Override
    public void render(ChemistrySetBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        int seed = (int) entity.getPos().asLong();
        matrices.push();
        Direction direction = entity.getCachedState().get(HorizontalFacingBlock.FACING);
        matrices.translate(0.5, 0.5, 0.5);
        matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(direction.asRotation()));
        //bottom center
        matrices.push();
        matrices.translate(-0.05, -0.10, -0.05);
        matrices.scale(0.3F, 0.3F, 0.3F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
        matrices.translate(0.3, 0, 0.3);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(1), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
        matrices.translate(-0.1, 0, -0.1);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(2), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
        matrices.pop();
        //bottom left
        matrices.push();
        matrices.translate(0.35, -0.2, -0.05);
        matrices.scale(0.3F, 0.3F, 0.3F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(3), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
        matrices.pop();
        //top right
        matrices.push();
        matrices.translate(-0.425, 0.2, 0);
        matrices.scale(0.3F, 0.3F, 0.3F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(4), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
        matrices.pop();
        //top left
        matrices.push();
        matrices.translate(0.3, 0.25, 0);
        matrices.scale(0.3F, 0.3F, 0.3F);
        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(5), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, seed);
        matrices.pop();
        matrices.pop();

    }

}

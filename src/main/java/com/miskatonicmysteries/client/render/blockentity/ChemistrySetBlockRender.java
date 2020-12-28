package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.common.block.blockentity.ChemistrySetBlockEntity;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;

public class ChemistrySetBlockRender extends BlockEntityRenderer<ChemistrySetBlockEntity> {
        public ChemistrySetBlockRender(BlockEntityRenderDispatcher dispatcher) {
                super(dispatcher);
        }

        @Override
        public void render(ChemistrySetBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
                int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
                matrices.push();
                Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(HorizontalFacingBlock.FACING);
                matrices.translate(0.5, 0.5, 0.5); //subtract 0.5 from everything below i guess
                matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(direction.asRotation()));
                //bottom center
                matrices.push();
                matrices.translate(-0.05, -0.10, -0.05);
                matrices.scale(0.3F, 0.3F, 0.3F);
                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            matrices.translate(0.3, 0, 0.3);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(1), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            matrices.pop();

            //bottom left
            matrices.push();
            matrices.translate(0.35, -0.2, -0.05);
            matrices.scale(0.3F, 0.3F, 0.3F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(2), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            matrices.pop();

            //top right
            matrices.push();
            matrices.translate(-0.425, 0.2, 0);
            matrices.scale(0.3F, 0.3F, 0.3F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(3), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            matrices.pop();

            //top left
            matrices.push();
            matrices.translate(0.3, 0.25, 0);
            matrices.scale(0.3F, 0.3F, 0.3F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(4), ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            matrices.pop();

            matrices.pop();

    }

}

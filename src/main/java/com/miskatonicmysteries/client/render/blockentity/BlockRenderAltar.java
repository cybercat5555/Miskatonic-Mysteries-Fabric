package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.ClientProxy;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityAltar;
import com.miskatonicmysteries.lib.Constants;
import net.minecraft.block.LecternBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

public class BlockRenderAltar extends BlockEntityRenderer<BlockEntityAltar> {
    private final BookModel book = new BookModel();

    public BlockRenderAltar(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BlockEntityAltar entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.getStack(0).isEmpty()) {
            matrixStack.push();
            matrixStack.translate(0.5D, 1.35D, 0.5D);
            float g = entity.getWorld().getBlockState(entity.getPos()).get(LecternBlock.FACING).rotateYClockwise().getOpposite().asRotation();
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-g));
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(67.5F));
            matrixStack.translate(0.0D, -2 * Constants.BLOCK_BIT, -Constants.BLOCK_BIT);
            matrixStack.scale(0.75F, 0.75F, 0.75F);
            this.book.setPageAngles(0.0F, 0.01F, 0.99F, 1.2F);
            VertexConsumer vertexConsumer = ClientProxy.getBookTextureFor(entity.getStack(0)).getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
            this.book.method_24184(matrixStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
        }
    }

}

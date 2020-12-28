package com.miskatonicmysteries.client.render.blockentity;

import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

public class OctagramBlockRender extends BlockEntityRenderer<OctagramBlockEntity> {
    public static final RenderLayer OCTAGRAM_RENDER_LAYER;

    public OctagramBlockRender(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Sprite sprite = ResourceHandler.getOctagramTextureFor(entity).getSprite();
        VertexConsumer buffer = sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(OCTAGRAM_RENDER_LAYER));
        matrixStack.push();
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(HorizontalFacingBlock.FACING);
        matrixStack.push();
        matrixStack.translate(0.5, 0, 0.5);
        matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(direction.getOpposite().asRotation()));
        matrixStack.translate(-1.5, 0.001, -1.5);

        Matrix4f mat = matrixStack.peek().getModel();
        buffer.vertex(mat, 0, 0, 3).color(1F, 1F, 1F, 1F).texture(sprite.getMinU(), sprite.getMaxV()).light(light).next();
        buffer.vertex(mat, 3, 0, 3).color(1F, 1F, 1F, 1F).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).next();
        buffer.vertex(mat, 3, 0, 0).color(1F, 1F, 1F, 1F).texture(sprite.getMaxU(), sprite.getMinV()).light(light).next();
        buffer.vertex(mat, 0, 0, 0).color(1F, 1F, 1F, 1F).texture(sprite.getMinU(), sprite.getMinV()).light(light).next();
        matrixStack.pop();

        matrixStack.translate(0.5F, 0, 0.5F);

        for (int i = 0; i < entity.size(); i++) {
            matrixStack.push();
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(0.125F * i * 360F));
            matrixStack.translate(0, 0, 1.1);
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(i), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumers);
            matrixStack.pop();
        }
        matrixStack.pop();
    }

    static {
        RenderLayer.MultiPhaseParameters param = RenderLayer.MultiPhaseParameters.builder()
                .shadeModel(new RenderPhase.ShadeModel(true))
                .texture(new RenderPhase.Texture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, false, true))
                .diffuseLighting(new RenderPhase.DiffuseLighting(true))
                .alpha(new RenderPhase.Alpha(0.04F))
                .lightmap(new RenderPhase.Lightmap(true))
                .build(true);
        OCTAGRAM_RENDER_LAYER = RenderLayer.of(Constants.MOD_ID + ":octagram_layer", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, GL11.GL_QUADS, 128, true, true, param);
    }

}

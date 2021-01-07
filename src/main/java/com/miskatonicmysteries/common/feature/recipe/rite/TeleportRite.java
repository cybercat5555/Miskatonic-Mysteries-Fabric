package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.lib.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public class TeleportRite extends Rite {
    private final int ticksNeeded;

    public TeleportRite() { //todo proper ingredient for this
        super(new Identifier(Constants.MOD_ID, "teleport"), Ingredient.ofItems(Items.ENDER_PEARL), Ingredient.ofItems(Items.ENDER_EYE));
        ticksNeeded = 60;
    }


    @Override
    public void tick(OctagramBlockEntity octagram) {
        if (!isFinished(octagram) && !octagram.permanentRiteActive) {
            super.tick(octagram);
        } //teleporting is handled in the Octagram Block
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        if (!octagram.getWorld().isClient) {
            octagram.tickCount = 0;
            //MMWorldState data = MMWorldState.get(octagram.getWorld());
            // data.addTeleport(octagram.getAffiliation(false).toString(), octagram.getPos(), (ServerWorld) octagram.getWorld());
        }
        super.onFinished(octagram);
    }


    @Override
    public void onCancelled(OctagramBlockEntity octagram) {
        if (!octagram.getWorld().isClient) {
            //    MMWorldState data = MMWorldState.get(octagram.getWorld());
            //    data.removeTeleport(octagram.getPos(), (ServerWorld) octagram.getWorld());
        }
        super.onCancelled(octagram);
    }


    @Override
    public boolean shouldContinue(OctagramBlockEntity octagram) {
        return super.shouldContinue(octagram);
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount >= ticksNeeded;
    }

    @Override
    public boolean isPermanent(OctagramBlockEntity octagram) {
        return true;
    }

    @Override
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        return super.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, dispatcher);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        Sprite sprite = ResourceHandler.getOctagramMaskTextureFor(entity).getSprite();
        float alpha = entity.permanentRiteActive ? 1 : entity.tickCount / (float) ticksNeeded;
        float[] origColors = entity.getAffiliation(true).getColor();
        float[] colors = {origColors[0], origColors[1], origColors[2], alpha};
        matrixStack.push();
        matrixStack.translate(0, 0.001F, 0);
        RenderHelper.renderTexturedPlane(3, sprite, matrixStack, sprite.getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(alpha < 1 ? RenderLayer.getTranslucent() : RenderLayer.getCutout())), light, overlay, new float[]{1, 1, 1, alpha});
        matrixStack.push();
        Matrix4f matrix4f = matrixStack.peek().getModel();
        double distance = entity.getPos().getSquaredDistance(dispatcher.camera.getPos(), true);
        int renderDepth = Math.max(RenderHelper.getDepthFromDistance(distance) - 14, 1);
        matrixStack.translate(1.5, 0, 1.5);
        matrixStack.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(45));
        matrixStack.translate(-0.4, 0.001F, -0.4);
        for (int i = 0; i < renderDepth; i++) {
            RenderHelper.renderPortalLayer(11 + i, entity.getWorld(), matrix4f, vertexConsumers, 0.8F, colors);
        }
        matrixStack.pop();
        matrixStack.translate(1.5, 0, 1.5);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(((float) entity.getWorld().getTime() + tickDelta) / 20.0F));
        matrixStack.translate(-1.5F, 0.0025F, -1.5F);
        RenderHelper.renderTexturedPlane(3, ResourceHandler.AURA_SPRITE.getSprite(), matrixStack, ResourceHandler.AURA_SPRITE.getSprite().getTextureSpecificVertexConsumer(vertexConsumers.getBuffer(RenderLayer.getTranslucent())), light, overlay, colors);
        matrixStack.pop();
    }
}

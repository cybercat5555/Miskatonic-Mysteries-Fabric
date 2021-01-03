package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.lib.ModParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class CraftingRite extends Rite {
    private int ticksNeeded;
    private ItemStack result;

    public CraftingRite(Identifier id, int ticksNeeded, ItemStack result, Ingredient... ingredients) {
        super(id, ingredients);
        this.ticksNeeded = ticksNeeded;
        this.result = result;
    }

    @Override
    public boolean isFinished(OctagramBlockEntity octagram) {
        return octagram.tickCount >= ticksNeeded;
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        super.onFinished(octagram);
        if (!octagram.getWorld().isClient) {
            Vec3d pos = octagram.getSummoningPos();
            octagram.getWorld().spawnEntity(new ItemEntity(octagram.getWorld(), pos.x, pos.y, pos.z, result));
        }
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        super.tick(octagram);
        float[] color = octagram.getAffiliation().getColor();
        octagram.getWorld().addParticle(ModParticles.MAGIC, octagram.getSummoningPos().x, octagram.getSummoningPos().y, octagram.getSummoningPos().z, color[0], color[1], color[2]);
    }

    @Override
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        return 1;
    }

    @Override
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
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
        matrixStack.push();
        //todo make flames gradually spawn where the items are, then let all of them slowly drift towards the center
    }
}

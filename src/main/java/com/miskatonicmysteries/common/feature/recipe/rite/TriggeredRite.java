package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.Affiliation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public abstract class TriggeredRite extends Rite {
    public final int ticksNeeded;

    public TriggeredRite(Identifier id, @Nullable Affiliation octagram, float investigatorChance, int tickCount, Ingredient... ingredients) {
        super(id, octagram, investigatorChance, ingredients);
        ticksNeeded = tickCount;
    }

    @Override
    public void tick(OctagramBlockEntity octagram) {
        super.tick(octagram);
        if (octagram.tickCount == ticksNeeded) {
            octagram.permanentRiteActive = true;
            octagram.clear();
            octagram.markDirty();
        }
    }

    public void trigger(OctagramBlockEntity octagram, Entity triggeringEntity) {
        octagram.triggered = true;
        octagram.tickCount = ticksNeeded;
    }

    @Override
    public void onFinished(OctagramBlockEntity octagram) {
        super.onFinished(octagram);
        octagram.permanentRiteActive = false;
        octagram.currentRite = null;
        octagram.tickCount = 0;
    }

    @Override
    public boolean isPermanent(OctagramBlockEntity octagram) {
        return true;
    }

    @Override
    public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        return !entity.triggered ? 2 : super.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, dispatcher);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay, BlockEntityRenderDispatcher dispatcher) {
        if (!entity.triggered) {
            float alpha = entity.tickCount >= ticksNeeded ? 1 : entity.tickCount / (float) ticksNeeded;
            matrixStack.translate(0, 0.001F, 0);
            RenderHelper.renderTexturedPlane(3, ResourceHandler.getOctagramTextureFor(entity).getSprite(), matrixStack, vertexConsumers.getBuffer(RenderHelper.getTransparency()), light, overlay, new float[]{1, 1, 1, Math.max(1 - alpha, 0.15F)});
        }
    }
}

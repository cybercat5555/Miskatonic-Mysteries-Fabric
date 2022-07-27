package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.render.RenderHelper;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.RiteCondition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public abstract class TriggeredRite extends Rite implements Triggerable{

	public final int ticksTillTriggerable;

	public TriggeredRite(Identifier id, @Nullable Affiliation octagram, float investigatorChance, int tickCount, RiteCondition... conditions) {
		super(id, octagram, investigatorChance, conditions);
		ticksTillTriggerable = tickCount;
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		super.tick(octagram);
		if (octagram.tickCount == ticksTillTriggerable) {
			octagram.permanentRiteActive = true;
			octagram.clear();
			octagram.markDirty();
		}
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
	@Environment(EnvType.CLIENT)
	public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
						   int light, int overlay, BlockEntityRendererFactory.Context context) {
		if (!entity.triggered) {
			float alpha = entity.tickCount >= ticksTillTriggerable ? 1 : entity.tickCount / (float) ticksTillTriggerable;
			matrixStack.translate(0, 0.001F, 0);
			RenderHelper.renderTexturedPlane(3, ResourceHandler.getOctagramTextureFor(entity).getSprite(), matrixStack,
											 vertexConsumers.getBuffer(RenderHelper.getTransparency()), light, overlay,
											 new float[]{1, 1, 1, Math.max(1 - alpha, 0.15F)});
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
							 int light, int overlay, BlockEntityRendererFactory.Context context) {
		return !entity.triggered ? 2 : super.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
	}

	@Override
	public void trigger(OctagramBlockEntity octagram, Entity triggeringEntity) {
		octagram.triggered = true;
		octagram.tickCount = ticksTillTriggerable;
	}

	@Override
	public boolean shouldTriggerFromStart(OctagramBlockEntity octagram, PlayerEntity player) {
		return false;
	}

	@Override
	public boolean isReadyToTrigger(OctagramBlockEntity octagram) {
		return !octagram.triggered && octagram.tickCount >= ticksTillTriggerable;
	}
}

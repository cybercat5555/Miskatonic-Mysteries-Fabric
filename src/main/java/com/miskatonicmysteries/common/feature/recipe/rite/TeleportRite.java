package com.miskatonicmysteries.common.feature.recipe.rite;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.item.IncantationYogItem;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.OctagramGateCondition;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TeleportRite extends Rite {

	private final int ticksNeeded;

	public TeleportRite() {
		super(new Identifier(Constants.MOD_ID, "teleport"), null, 0, new OctagramGateCondition());
		ticksNeeded = 60;
	}

	@Override
	public void tick(OctagramBlockEntity octagram) {
		if (!isFinished(octagram) && !octagram.permanentRiteActive) {
			super.tick(octagram);
		}
	}

	@Override
	public boolean isFinished(OctagramBlockEntity octagram) {
		return octagram.tickCount >= ticksNeeded;
	}

	@Override
	public void onFinished(OctagramBlockEntity octagram) {
		if (!octagram.getWorld().isClient) {
			ServerWorld world = (ServerWorld) octagram.getWorld();
			octagram.tickCount = 0;
			ItemStack incantation = octagram.getStack(MMObjects.INCANTATION_YOG);
			if (!incantation.isEmpty()) {
				BlockPos octagramPos = IncantationYogItem.getPosition(incantation);
				ServerWorld boundWorld = IncantationYogItem.getWorld(world, incantation);
				if (boundWorld.getBlockEntity(octagramPos) instanceof OctagramBlockEntity) {
					octagram.bind(boundWorld, octagramPos);
					OctagramBlockEntity otherOctagram = (OctagramBlockEntity) boundWorld.getBlockEntity(octagramPos);
					otherOctagram.bind(world, octagram.getPos());
					otherOctagram.permanentRiteActive = true;
					otherOctagram.currentRite = this;
					otherOctagram.tickCount = 0;
					otherOctagram.markDirty();
					otherOctagram.sync(octagram.getWorld(), octagram.getPos());
				}
			}
		}
		super.onFinished(octagram);
	}

	@Override
	public void onCancelled(OctagramBlockEntity octagram) {
		OctagramBlockEntity otherOctagram = octagram.getBoundOctagram();
		if (otherOctagram != null && !otherOctagram.getWorld().isClient) {
			otherOctagram.permanentRiteActive = false;
			otherOctagram.currentRite = null;
			otherOctagram.tickCount = 0;
			octagram.boundPos = null;
			otherOctagram.boundPos = null;
			otherOctagram.markDirty();
			octagram.markDirty();
			otherOctagram.sync(octagram.getWorld(), octagram.getPos());
		}
		super.onCancelled(octagram);
	}

	@Override
	public boolean isPermanent(OctagramBlockEntity octagram) {
		return true;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void renderRite(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
						   int light, int overlay, BlockEntityRendererFactory.Context context) {
		float alpha = entity.permanentRiteActive ? 1 : entity.tickCount / (float) ticksNeeded;
		renderPortalOctagram(alpha, entity.getAffiliation(true).getColor(), entity, tickDelta, matrixStack, vertexConsumers, light, overlay,
							 context);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public byte beforeRender(OctagramBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers,
							 int light, int overlay, BlockEntityRendererFactory.Context context) {
		return super.beforeRender(entity, tickDelta, matrixStack, vertexConsumers, light, overlay, context);
	}

	@Override
	public float getInstabilityBase(OctagramBlockEntity blockEntity) {
		return 0.05F;
	}
}

package com.miskatonicmysteries.client.render.entity;

import com.miskatonicmysteries.common.feature.entity.HallucinationEntity;
import com.miskatonicmysteries.common.registry.MMEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HallucinationRenderer extends EntityRenderer<HallucinationEntity> {

	public HallucinationRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public boolean shouldRender(HallucinationEntity entity, Frustum frustum, double x, double y, double z) {
		return !entity.isInvisibleTo(MinecraftClient.getInstance().player) && super.shouldRender(entity, frustum, x, y, z);
	}


	@Override
	public void render(HallucinationEntity entity, float yaw, float tickDelta, MatrixStack matrices,
		VertexConsumerProvider vertexConsumers, int light) {
		EntityType<?> type = entity.getEntityHallucination();
		if (type != null && type != MMEntities.HALLUCINATION) {
			Entity rendererHallucination = type.create(entity.world);
			if (rendererHallucination instanceof LivingEntity living) {
				living.setId(entity.getId());
				living.age = entity.age;
				living.hurtTime = entity.hurtTime;
				living.maxHurtTime = 2147483647;
				living.limbDistance = entity.limbDistance;
				living.lastLimbDistance = entity.lastLimbDistance;
				living.limbAngle = entity.limbAngle;
				living.headYaw = entity.headYaw;
				living.prevHeadYaw = entity.prevHeadYaw;
				living.bodyYaw = entity.bodyYaw;
				living.prevBodyYaw = entity.prevBodyYaw;
				living.handSwinging = entity.handSwinging;
				living.handSwingTicks = entity.handSwingTicks;
				living.handSwingProgress = entity.handSwingProgress;
				living.lastHandSwingProgress = entity.lastHandSwingProgress;
				living.setPitch(entity.getPitch());
				living.prevPitch = entity.prevPitch;
				living.preferredHand = entity.preferredHand;
				living.setStackInHand(Hand.MAIN_HAND, entity.getMainHandStack());
				living.setStackInHand(Hand.OFF_HAND, entity.getOffHandStack());
				living.setCurrentHand(entity.getActiveHand() == null ? Hand.MAIN_HAND : entity.getActiveHand());
				living.setSneaking(entity.isSneaking());
				living.setPose(entity.getPose());
				if (entity.hasVehicle()) {
					living.startRiding(entity.getVehicle(), true);
				}
				MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(living)
					.render(living, yaw, tickDelta, matrices, vertexConsumers, light);
			}
		}
	}

	@Override
	public Identifier getTexture(HallucinationEntity entity) {
		return null;
	}
}

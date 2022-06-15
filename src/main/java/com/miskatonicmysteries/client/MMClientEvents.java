package com.miskatonicmysteries.client;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.sound.ResonatorSound;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.mixin.client.WorldRendererAccessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class MMClientEvents {

	public static boolean glowingActive = false;
	private static boolean renderOutline = false;

	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(MMClientEvents::handleSounds);
		WorldRenderEvents.BEFORE_DEBUG_RENDER.register(MMClientEvents::handleOutlineShader);
		WorldRenderEvents.AFTER_ENTITIES.register(MMClientEvents::renderAfterEntities);
	}


	private static void handleSounds(MinecraftClient minecraftClient) {
		for (BlockPos blockPos : ResonatorSound.soundInstances.keySet()) {
			if (ResonatorSound.soundInstances.get(blockPos).isDone()) {
				ResonatorSound.soundInstances.remove(blockPos);
			}
		}
	}

	private static void handleOutlineShader(WorldRenderContext context) {
		if (renderOutline && !glowingActive) {
			MinecraftClient client = MinecraftClient.getInstance();
			((WorldRendererAccessor) context.worldRenderer()).getEntityOutlineShader().render(context.tickDelta());
			client.getFramebuffer().beginWrite(false);
		}
		glowingActive = false;
	}

	private static void renderAfterEntities(WorldRenderContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		renderOutline = false;
		if (client.player != null && MinecraftClient.getInstance().player.hasStatusEffect(MMStatusEffects.CLAIRVOYANCE)) {
			for (Entity entity : context.world().getEntities()) {
				if ((entity == client.player && !context.camera().isThirdPerson())) {
					continue;
				}
				Affiliation affiliation = MiskatonicMysteriesAPI.getNonNullAffiliation(entity, false);
				if (entity instanceof Monster || entity instanceof PlayerEntity || entity instanceof Affiliated) {
					renderOutline = true;
					OutlineVertexConsumerProvider outlineVertexConsumerProvider = client.getBufferBuilders().getOutlineVertexConsumers();
					int color = affiliation == MMAffiliations.NONE ? affiliation.textColor : affiliation.getIntColor();
					int r = color >> 16 & 255;
					int g = color >> 8 & 255;
					int b = color & 255;
					outlineVertexConsumerProvider.setColor(r, g, b, 255);
					Vec3d cameraPos = context.camera().getPos();
					renderEntityFullBright(entity, cameraPos.x, cameraPos.y, cameraPos.z, context.tickDelta(), context.matrixStack(),
										   outlineVertexConsumerProvider, client.getEntityRenderDispatcher());
				}
			}
		}
	}

	private static void renderEntityFullBright(Entity entity, double cameraX, double cameraY, double cameraZ,
											   float tickDelta, MatrixStack matrices,
											   VertexConsumerProvider vertexConsumers,
											   EntityRenderDispatcher dispatcher) {
		double d = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
		double e = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
		double f = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
		float g = MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw());
		dispatcher.render(entity, d - cameraX, e - cameraY, f - cameraZ, g, tickDelta, matrices, vertexConsumers,
						  15728880);
	}
}

package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.client.gui.hud.CurrentSpellHUD;
import com.miskatonicmysteries.client.gui.hud.SpellBurnoutHUD;
import com.miskatonicmysteries.client.render.blockentity.OctagramBlockRender;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.Tags;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

public class HudHandler {
	private static final Identifier CHECKMARK = new Identifier(Constants.MOD_ID, "textures/gui/check.png");
	private static final Identifier VALID_SACRIFICE = new Identifier(Constants.MOD_ID, "textures/gui/rite_conditions/sacrifice.png");
	public static SpellBurnoutHUD burnoutHUD;
	public static CurrentSpellHUD currentSpellHUD;

	public static void init() {
		burnoutHUD = new SpellBurnoutHUD();
		currentSpellHUD = new CurrentSpellHUD();
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			burnoutHUD.render(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), client.player);
			currentSpellHUD
				.render(client, matrixStack, tickDelta, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(),
						client.player);
			OctagramBlockRender.handleDisplay(client, matrixStack, tickDelta);
			renderIsSacrificable(client, matrixStack);
		});
	}

	private static void renderIsSacrificable(MinecraftClient client, MatrixStack matrixStack) {
		if (client.targetedEntity != null && client.player != null) {
			if (client.player.isHolding(MMObjects.NECRONOMICON) && client.targetedEntity.getType().isIn(Tags.VALID_SACRIFICES)) {
				matrixStack.push();
				matrixStack.translate(client.getWindow().getScaledWidth() / 2.0 - 1, client.getWindow().getScaledHeight() / 2.0 + 12, 0);
				drawIcon(matrixStack, true, VALID_SACRIFICE);
				matrixStack.pop();
			}
		}
	}

	public static void drawIcon(MatrixStack matrixStack, boolean checked, Identifier texture) {
		Matrix4f matrix = matrixStack.peek().getPositionMatrix();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderTexture(0, texture);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(matrix, -4, 4, 0).texture(0, 1).next();
		bufferBuilder.vertex(matrix, 4, 4, 0).texture(1, 1).next();
		bufferBuilder.vertex(matrix, 4, -4, 0).texture(1, 0).next();
		bufferBuilder.vertex(matrix, -4, -4, 0).texture(0, 0).next();
		tessellator.draw();
		if (checked) {
			RenderSystem.setShaderTexture(0, CHECKMARK);
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
			bufferBuilder.vertex(matrix, -2, 6, 0).texture(0, 1).next();
			bufferBuilder.vertex(matrix, 6, 6, 0).texture(1, 1).next();
			bufferBuilder.vertex(matrix, 6, -2, 0).texture(1, 0).next();
			bufferBuilder.vertex(matrix, -2, -2, 0).texture(0, 0).next();
			tessellator.draw();
		}
	}
}

package com.miskatonicmysteries.client.gui.hud;

import com.miskatonicmysteries.api.interfaces.SpellCaster;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import com.mojang.blaze3d.systems.RenderSystem;

@Environment(EnvType.CLIENT)
public class SpellBurnoutHUD extends DrawableHelper {

	public static final Identifier VIGNETTE_TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/burnout.png");

	public void render(MinecraftClient client, int scaledWidth, int scaledHeight, PlayerEntity player) {
		SpellCaster.of(player).ifPresent(caster -> {
			if (caster.getSpellCooldown() > 0) {
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				float burnout = MathHelper.clamp(caster.getSpellCooldown() / 80F, 0, 0.5F);
				RenderSystem.enableTexture();
				RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
				BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderColor(1F, 1F, 1F, burnout);
				bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
				bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.end();
				BufferRenderer.draw(bufferBuilder);
				RenderSystem.disableBlend();
			/*
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.enableTexture();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder bufferBuilder = tessellator.getBuffer();
				RenderSystem.setShaderColor(1, 1, 1, burnout);
				bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
				RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
				bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(0.0F, 1.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(1.0F, 1.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(1.0F, 0.0F).color(1F, 1F, 1F, burnout).next();
				bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(0.0F, 0.0F).color(1F, 1F, 1F, burnout).next();
				tessellator.draw();
				RenderSystem.depthMask(true);
				RenderSystem.enableDepthTest();
				RenderSystem.disableTexture();
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);*/
			}
		});
	}
}

package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.SudokuScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

import com.mojang.blaze3d.systems.RenderSystem;

@Environment(EnvType.CLIENT)
public class SudokuInfoWidget extends ClickableWidget {

	private SudokuScreen screen;

	public SudokuInfoWidget(int x, int y, SudokuScreen screen) {
		super(x, y, 16, 16, NarratorManager.EMPTY);
		this.screen = screen;
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, screen.alpha > 0.75 && isHovered() && isValidClickButton(0) ? 0.75F : screen.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderTexture(0, screen.style);
		matrices.push();
		if (screen.finished) {
			matrices.translate(0, -screen.finishingTicks, 0);
		}
		SudokuScreen.drawTexture(matrices, this.x, this.y, width, height, 73, 0, 8, 8,
								 128, 128);
		matrices.pop();

		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		if (screen.infoTicks < 10) {
			screen.infoTicks = 200;
		} else if (screen.infoTicks > 10) {
			screen.infoTicks = 10;
		}
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return !screen.finished && super.isValidClickButton(button);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		super.appendDefaultNarrations(builder);
	}
}

package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import com.mojang.blaze3d.systems.RenderSystem;

@Environment(EnvType.CLIENT)
public class SelectPredicateTypeButton extends ClickableWidget {

	private final ConfigurePredicateScreen screen;
	private final Identifier category;
	private final Identifier texture;

	public SelectPredicateTypeButton(int x, int y, ConfigurePredicateScreen screen, Identifier category) {
		super(x, y, 18, 18, NarratorManager.EMPTY);
		this.screen = screen;
		this.category = category;
		this.texture = getIconTextureLocation(category);
	}

	public Identifier getIconTextureLocation(Identifier category) {
		return new Identifier(category.getNamespace(), String.format("textures/gui/icons/%s.png", category.getPath()));
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
		drawTexture(matrices, this.x, this.y, 94, isSelected() ? 0 : 18, 18, 18, 128, 128);
		RenderSystem.setShaderTexture(0, texture);
		drawTexture(matrices, this.x + 1, this.y + 1, 0, 0, 16, 16, 16, 16);
		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
	}

	private boolean isSelected() {
		return screen.getCurrentCategory() != null && screen.getCurrentCategory() == category;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
		screen.switchCurrentCategory(category);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		super.appendDefaultNarrations(builder);
	}
}

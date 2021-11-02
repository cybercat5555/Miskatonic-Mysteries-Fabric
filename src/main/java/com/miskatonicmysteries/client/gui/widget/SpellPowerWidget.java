package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.EditSpellScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class SpellPowerWidget extends ClickableWidget {

	public EditSpellScreen screen;
	public int power;

	public SpellPowerWidget(int x, int y, int power, EditSpellScreen screen) {
		super(x, y, 32, 32, NarratorManager.EMPTY);
		this.screen = screen;
		this.power = power;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (screen.power == power) {
			screen.power = -1;
		} else if (isValidClickButton(0)) {
			screen.power = power;
		}
	}

	public boolean isSelected() {
		return this.power == screen.power;
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return screen.availablePower >= power + 1 && super.isValidClickButton(button);
	}

	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		RenderSystem.setShaderTexture(0, EditSpellScreen.BOOK_TEXTURE);
		RenderSystem
			.setShaderColor(1.0F, 1.0F, 1.0F, isHovered() && isValidClickButton(0) ? 0.75F : !isValidClickButton(0) ? 0.25F : this.alpha);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();

		//change texture depending on allocated power and if pressed
		drawTexture(matrices, this.x, this.y, 60 + 32 * power, 182 + (isSelected() ? 39 : 0), this.width, this.height, 512, 256);

		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		super.appendDefaultNarrations(builder);
	}
}

package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.HasturSudokuScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SudokuTileWidget extends ClickableWidget {
	public HasturSudokuScreen screen;
	public int index;
	public boolean immutable;

	public SudokuTileWidget(int x, int y, int index, boolean immutable, HasturSudokuScreen screen) {
		super(x, y, 30, 30, NarratorManager.EMPTY);
		this.screen = screen;
		this.index = index;
		this.immutable = immutable;
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		if (!immutable) {
			screen.cycleTile(index);
			if (screen.isSolved()) {
				screen.finish();
			}
		}
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return !immutable && !screen.finished && super.isValidClickButton(button);
	}


	@Override
	public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		byte value = screen.tiles[index % 4][index / 4];
		if (value > 0) {
			matrices.push();
			if (!screen.finished) {
				RenderSystem
					.setShaderColor(1.0F, 1.0F, 1.0F, screen.alpha > 0.75 && isHovered() && isValidClickButton(0) ? 0.75F : screen.alpha);
			}else {
				int ownTick = Math.max(screen.finishingTicks - index * 5, 0);
				float progress = Math.min(ownTick / 20F, 1);
				RenderSystem
					.setShaderColor(1.0F, 1.0F, 1.0F, 1 - progress);
				Vec3d vec = new Vec3d(screen.width / 2F - x - 16, screen.height / 2F - y - 16, 0);
				matrices.translate(vec.getX() * progress, vec.getY() * progress, 0);
			}
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			RenderSystem.setShaderTexture(0, HasturSudokuScreen.TEXTURE);
			HasturSudokuScreen.drawTexture(matrices, this.x, this.y, width, height, (value - 1) * 16, immutable ? 89 : 73, 15, 15,
				128, 128);
			matrices.pop();
		}
		this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {
		super.appendDefaultNarrations(builder);
	}
}

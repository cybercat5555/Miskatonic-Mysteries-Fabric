package com.miskatonicmysteries.client.gui.toast;

import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleIconToast implements Toast {
	private final List<Identifier> icons = new ArrayList<>();
	private final List<String> translationStrings = new ArrayList<>();
	private long startTime;
	private boolean justUpdated;

	public SimpleIconToast(Identifier icon, String translation) {
		this.icons.add(icon);
		this.translationStrings.add(translation);
	}

	@Override
	public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
		if (this.justUpdated) {
			this.startTime = startTime;
			this.justUpdated = false;
		}
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, MMAffiliations.NONE.getToastTextureLocation());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
		manager.getGame().textRenderer.draw(matrices, getTitle(), 30.0F, 7.0F,  MMAffiliations.NONE.textColor);
		manager.getGame().textRenderer.draw(matrices, getDescription(startTime), 30.0F, 18.0F,  MMAffiliations.NONE.textColorSecondary);
		Identifier icon =
				this.icons.get((int) (startTime / Math.max(1L, 5000L / (long) this.icons.size()) % (long) this.icons.size()));
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderTexture(0, icon);
		ToastManager.drawTexture(matrices, getDisplayedWidth() / 2, getDisplayedHeight() / 2, 0, 0,
				getDisplayedWidth(), getDisplayedHeight(), getDisplayedWidth(), getDisplayedHeight());
		return startTime - this.startTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

	protected int getDisplayedWidth() {
		return 16;
	}

	protected int getDisplayedHeight() {
		return 16;
	}

	protected abstract Text getTitle();

	protected Text getDescription(long startTime) {
		return new TranslatableText(this.translationStrings.get((int) (startTime / Math.max(1L,
				5000L / (long) this.translationStrings.size()) % (long) this.translationStrings.size())));
	}

	protected void addIcon(Identifier icon, String translationString) {
		this.icons.add(icon);
		this.translationStrings.add(translationString);
		justUpdated = true;
	}
}

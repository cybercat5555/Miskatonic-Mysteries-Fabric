package com.miskatonicmysteries.client.gui.toast;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BlessingToast implements Toast {
	private static final Text TITLE = new TranslatableText("blessing.miskatonicmysteries.toast.title");
	private static final Identifier ICON = ResourceHandler.ASCENSION_STAR_SPRITE;
	private final List<Blessing> blessings = new ArrayList<>();
	private long startTime;
	private boolean justUpdated;

	public BlessingToast(Blessing blessing) {
		this.blessings.add(blessing);
	}

	@Override
	public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
		if (this.justUpdated) {
			this.startTime = startTime;
			this.justUpdated = false;
		}
		Blessing blessing =
				this.blessings.get((int) (startTime / Math.max(1L, 5000L / (long) this.blessings.size()) % (long) this.blessings.size()));
		Affiliation flavor = blessing.getAffiliation(false);
		if (flavor == null) {
			flavor = MiskatonicMysteriesAPI.getNonNullAffiliation(MinecraftClient.getInstance().player, false);
		}
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, flavor.getToastTextureLocation());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		manager.drawTexture(matrices, 0, 0, 0, 0, this.getWidth(), this.getHeight());
		manager.getGame().textRenderer.draw(matrices, getTitle(), 30.0F, 7.0F, flavor.textColor);
		manager.getGame().textRenderer.draw(matrices, new TranslatableText(blessing.getTranslationString()), 30.0F,
				18.0F, flavor.textColorSecondary);
		RenderSystem.applyModelViewMatrix();
		RenderSystem.setShaderTexture(0, ICON);
		ToastManager.drawTexture(matrices, 12, 12, 0, 0, 8, 8, 8, 8);
		return startTime - this.startTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

	protected Text getTitle() {
		return TITLE;
	}

	protected void addBlessing(Blessing blessing) {
		this.blessings.add(blessing);
		justUpdated = true;
	}

	public static void show(ToastManager manager, Blessing blessing) {
		BlessingToast toast = manager.getToast(BlessingToast.class, TYPE);
		if (toast == null) {
			manager.add(new BlessingToast(blessing));
		}
		else {
			toast.addBlessing(blessing);
		}
	}
}

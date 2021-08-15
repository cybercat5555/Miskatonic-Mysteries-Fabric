package com.miskatonicmysteries.client.gui.toast;

import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class KnowledgeToast implements Toast {
	private static final Text TITLE = new TranslatableText("knowledge.miskatonicmysteries.toast.title");
	private final List<String> knowledge = new ArrayList<>();
	private long startTime;
	private boolean justUpdated;

	public KnowledgeToast(String firstKnowledge) {
		knowledge.add(firstKnowledge);
	}

	public static void show(ToastManager manager, String knowledge) {
		KnowledgeToast toast = manager.getToast(KnowledgeToast.class, TYPE);
		if (toast == null) {
			manager.add(new KnowledgeToast(knowledge));
		}
		else {
			toast.setKnowledge(knowledge);
		}
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
		manager.getGame().textRenderer.draw(matrices, getTitle(), 30.0F, 7.0F, MMAffiliations.NONE.textColor);
		String string =
				this.knowledge.get((int) (startTime / Math.max(1L, 5000L / (long) this.knowledge.size()) % (long) this.knowledge.size()));
		manager.getGame().textRenderer.draw(matrices, new TranslatableText("knowledge.miskatonicmysteries." + string),
				30.0F, 18.0F, MMAffiliations.NONE.textColorSecondary);
		RenderSystem.applyModelViewMatrix();
		manager.getGame().getItemRenderer().renderInGui(MMObjects.NECRONOMICON.getDefaultStack(), 8, 8);
		return startTime - this.startTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
	}

	protected Text getTitle() {
		return TITLE;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge.add(knowledge);
		justUpdated = true;
	}
}

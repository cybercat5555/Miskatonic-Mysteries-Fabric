package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.Ascendant;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class BlessingInfoWidget extends TexturedButtonWidget {

	private static final Text TITLE = new TranslatableText("miskatonicmysteries.gui.blessings");
	private boolean expanded = false;
	private float expandTicks;

	public BlessingInfoWidget(int x, int y) {
		super(x + 1, y - 51, 9, 8, 0, 0, ResourceHandler.ASCENSION_STAR_SPRITE, ButtonWidget::onPress);
		visible = Ascendant.of(MinecraftClient.getInstance().player).map(ascendant -> !ascendant.getBlessings().isEmpty()).orElse(false);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (visible) {
			if (expanded && expandTicks < 40) {
				expandTicks += 1 + delta;
				if (expandTicks > 40) {
					expandTicks = 40;
				}
			} else if (!expanded && expandTicks > 0) {
				expandTicks -= 1 + delta;
				if (expandTicks < 0) {
					expandTicks = 0;
				}
			}
			Affiliation affiliation = MiskatonicMysteriesAPI.getNonNullAffiliation(MinecraftClient.getInstance().player, false);
			Affiliation affiliationApparent = MiskatonicMysteriesAPI.getNonNullAffiliation(MinecraftClient.getInstance().player, true);
			int reach = expandTicks > 10 ? Math.round((expandTicks - 10) / 30F * 63) : 0;
			int width = expandTicks < 10 ? Math.round(expandTicks / 10F * 22) : 22;
			matrices.push();
			matrices.translate(x + 0.5, y, 0);
			matrices.push();
			matrices.translate(3, 3, 0);
			DrawableHelper.fill(matrices, -width, 1, width, reach, 0xDD000000);
			DrawableHelper.fill(matrices, -width, 0, width, 1, affiliation.getIntColor());
			DrawableHelper.fill(matrices, -width, reach, width, reach + 1, affiliationApparent.getIntColor());
			matrices.pop();
			RenderSystem.setShaderTexture(0, ResourceHandler.ASCENSION_STAR_SPRITE);
			RenderSystem.setShaderColor(affiliation.getColor()[0], affiliation.getColor()[1], affiliation.getColor()[2], 1);
			DrawableHelper.drawTexture(matrices, 0, 0, 8, 8, 0, 0, 8, 8, 8, 8); //draw star
			RenderSystem
				.setShaderColor(affiliationApparent.getColor()[0], affiliationApparent.getColor()[1], affiliationApparent.getColor()[2], 1);
			DrawableHelper.drawTexture(matrices, 0, reach, 8, 8, 0, 0, 8, 8, 8, 8); //draw star
			if (expandTicks >= 40) {
				RenderSystem.enableDepthTest();
				TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
				matrices.translate(2.5F, 7, 100);
				matrices.scale(0.75F, 0.75F, 1F);
				drawCenteredText(matrices, textRenderer, TITLE, 0, 0, 0xFFFFFF);
				matrices.translate(0, 4, 0);
				matrices.scale(0.65F, 0.65F, 1F);
				Ascendant.of(MinecraftClient.getInstance().player).ifPresent(ascendant -> {
					for (Blessing blessing : ascendant.getBlessings()) {
						matrices.translate(0, 15, 0);
						drawCenteredText(matrices, textRenderer, new TranslatableText(blessing.getTranslationString()), 0, 0, 0xFFFFFF);
					}
				});
			}
			matrices.pop();
			RenderSystem.setShaderColor(1, 1, 1, 1);
		}
	}


	@Override
	public void onPress() {
		expanded = !expanded;
	}
}

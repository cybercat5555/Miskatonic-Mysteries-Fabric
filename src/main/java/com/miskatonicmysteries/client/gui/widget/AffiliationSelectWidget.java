package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.common.registry.MMRegistries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.mojang.blaze3d.systems.RenderSystem;

public class AffiliationSelectWidget extends ClickableWidget {

	private final List<Affiliation> affiliations = new ArrayList<>();
	private final Consumer<Affiliation> onChanged;
	private Affiliation selectedAffiliation;

	public AffiliationSelectWidget(int x, int y, Affiliation selected, Consumer<Affiliation> onChanged) {
		super(x, y, 90, 18, Text.of(""));
		this.selectedAffiliation = selected;
		this.affiliations.addAll(MMRegistries.AFFILIATIONS.stream().collect(Collectors.toList()));
		this.height = 18 * (affiliations.size() / 5);
		this.onChanged = onChanged;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
		drawTexture(matrices, x - 2, y - 18, 0, 0, 94, 40, 128, 128);
		drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, selectedAffiliation.getLocalizedName(),
						 x + width / 2, y - 14, selectedAffiliation.textColor);
		for (int i = 0; i < affiliations.size(); i++) {
			Affiliation affiliation = affiliations.get(i);
			int x = this.x + (i % 5) * 18;
			int y = this.y + (i / 5) * 18;

			if (selectedAffiliation == affiliation) {
				RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
				drawTexture(matrices, x, y, 0, 40, 18, 18, 128, 128);
			}
			RenderSystem.setShaderTexture(0, affiliations.get(i).getIconTextureLocation());
			drawTexture(matrices, x + 1, y + 1, 0, 0, 16, 16, 16, 16);
		}
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		if (super.clicked(mouseX, mouseY)) {
			int x = (int) (mouseX - this.x);
			int y = (int) (mouseY - this.y);
			int index = x / 18 + y / 18 * 5;
			if (affiliations.size() > index) {
				this.selectedAffiliation = affiliations.get(index);
				onChanged.accept(selectedAffiliation);
				return true;
			}
		}
		return false;
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}
}

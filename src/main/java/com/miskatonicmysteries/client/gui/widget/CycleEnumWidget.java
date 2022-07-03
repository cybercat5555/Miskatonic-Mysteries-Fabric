package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.common.registry.MMAffiliations;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Function;

import com.mojang.blaze3d.systems.RenderSystem;

public class CycleEnumWidget<T extends Enum<T>> extends ClickableWidget {

	private final Consumer<Enum<T>> onChanged;
	private final Function<Enum<T>, Text> toText;
	private final Enum<T>[] options;
	private final int distance;
	private int currentIndex;

	public CycleEnumWidget(int startIndex, int x, int y, int distance, Consumer<Enum<T>> onChanged, Function<Enum<T>, Text> toText,
						   Enum<T>... options) {
		super(x - 6, y, 12, 12, Text.of(""));
		this.onChanged = onChanged;
		this.toText = toText;
		this.options = options;
		this.currentIndex = startIndex;
		this.distance = distance;
	}


	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		//super.render(matrices, mouseX, mouseY, delta);
		RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
		drawTexture(matrices, x - distance, y, 19, 40, width, height, 128, 128);
		drawTexture(matrices, x + distance, y, 30, 40, width, height, 128, 128);
		drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, toText.apply(options[currentIndex]), x + 6, y + 2,
						 MMAffiliations.NONE.textColor);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);

		if (mouseX > x + width) {
			currentIndex++;
			if (currentIndex >= options.length) {
				currentIndex = 0;
			}
		} else {
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = options.length - 1;
			}
		}

		onChanged.accept(options[currentIndex]);
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return super.isValidClickButton(button);
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return super.clicked(mouseX - distance, mouseY) || super.clicked(mouseX + distance, mouseY);
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder builder) {

	}
}

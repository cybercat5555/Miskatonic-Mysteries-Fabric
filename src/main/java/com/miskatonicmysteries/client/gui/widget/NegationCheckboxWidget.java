package com.miskatonicmysteries.client.gui.widget;

import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.common.handler.predicate.ConfigurablePredicate;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;

public class NegationCheckboxWidget extends CheckboxWidget {
	private final Consumer<Boolean> onChanged;
	public NegationCheckboxWidget(int x, int y, boolean startState, Text text, Consumer<Boolean> onChanged) {
		super(x, y, 12, 12, text, startState, false);
		this.onChanged = onChanged;
	}

	@Override
	public void onPress() {
		super.onPress();
		onChanged.accept(isChecked());
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		RenderSystem.setShaderTexture(0, ConfigurePredicateScreen.TEXTURE);
		DrawableHelper.drawTexture(matrices, x, y,  isChecked() ? 42 : 54, 40, width, height,128, 128);
		drawTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, getMessage(), x - 68, y + 2, MMAffiliations.NONE.textColor);
	}
}

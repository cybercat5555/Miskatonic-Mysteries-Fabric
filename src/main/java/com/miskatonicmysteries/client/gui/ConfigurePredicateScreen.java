package com.miskatonicmysteries.client.gui;

import com.miskatonicmysteries.api.interfaces.HasConfigurablePredicate;
import com.miskatonicmysteries.api.registry.ConfigurablePredicateType;
import com.miskatonicmysteries.client.gui.widget.SelectPredicateTypeButton;
import com.miskatonicmysteries.common.handler.predicate.ConfigurablePredicate;
import com.miskatonicmysteries.common.util.Constants;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ConfigurePredicateScreen extends Screen {

	public static final Identifier TEXTURE = new Identifier(Constants.MOD_ID, "textures/gui/configuration.png");
	private final HasConfigurablePredicate source;
	private final ConfigurablePredicateType type;
	private final ConfigurablePredicate currentPredicate;

	private Identifier currentCategory;

	public ConfigurePredicateScreen(HasConfigurablePredicate source, ConfigurablePredicate predicate, ConfigurablePredicateType type) {
		super(new TranslatableText(Constants.MOD_ID + ".gui.configure_predicates"));
		this.source = source;
		this.currentPredicate = predicate.copy();
		this.type = type;
	}

	@Override
	public void close() {
		super.close();
		source.finishConfiguration(currentPredicate);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		super.init();
		Identifier[] cats = type.getCategories();
		int distance = width / (1 + cats.length);
		for (int i = 0; i < cats.length; i++) {
			addDrawableChild(new SelectPredicateTypeButton(distance * (1 + i) - 9, 60, this, cats[i]));
		}
		if (currentPredicate != null && currentCategory != null) {
			currentPredicate.addWidgets(this, currentCategory);
		}
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_TAB && getFocused() instanceof TextFieldWidget) {
			return this.getFocused().keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	public <T extends Element & Drawable & Selectable> void addWidgetExternally(T element) {
		addDrawableChild(element);
	}

	public void addDrawableExternally(Drawable element) {
		addDrawable(element);
	}

	public Identifier getCurrentCategory() {
		return currentCategory;
	}

	public void switchCurrentCategory(Identifier category) {
		this.currentCategory = category;
		clearChildren();
		init();
	}
}

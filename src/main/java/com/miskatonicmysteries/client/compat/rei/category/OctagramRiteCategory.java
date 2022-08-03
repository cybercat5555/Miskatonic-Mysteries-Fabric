package com.miskatonicmysteries.client.compat.rei.category;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.compat.rei.MMREICompat;
import com.miskatonicmysteries.client.gui.HudHandler;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.feature.recipe.rite.TriggeredRite;
import com.miskatonicmysteries.common.feature.recipe.rite.condition.RiteCondition;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Util;

import net.minecraft.client.gui.Element;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Label;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import org.jetbrains.annotations.NotNull;

public class OctagramRiteCategory implements DisplayCategory<OctagramRiteCategory.OctagramDisplay> {

	public static final TranslatableText TITLE = new TranslatableText("rei.miskatonicmysteries.octagram_rite");
	public static final Identifier ID = new Identifier(Constants.MOD_ID, "octagram_rite");
	public static final EntryStack<ItemStack> ICON = EntryStacks.of(MMObjects.SHUB_CHALK);

	@Override
	public Renderer getIcon() {
		return ICON;
	}

	@Override
	public Text getTitle() {
		return TITLE;
	}

	@Override
	public @NotNull List<Widget> setupDisplay(OctagramDisplay recipeDisplay, Rectangle bounds) {
		List<Widget> widgets = new ArrayList<>();
		Point startPoint = new Point(bounds.getCenterX() - 32, bounds.getCenterY() - 6);
		widgets.add(Widgets.createRecipeBase(bounds));
		SpriteIdentifier sprite = ResourceHandler.getMatchingOctagramTexture(recipeDisplay.getRite().getOctagramAffiliation());
		widgets.add(Widgets.createTexturedWidget(
			new Identifier(sprite.getTextureId().getNamespace(), "textures/" + sprite.getTextureId().getPath() + ".png"), startPoint.x,
			startPoint.y - 24, 0, 0, 64, 64, sprite.getSprite().getWidth(), sprite.getSprite().getHeight(), sprite.getSprite().getWidth(),
			sprite.getSprite().getHeight()));
		int size = recipeDisplay.getInputEntries().size();
		if (size > 0) { //peak elegance haha yes
			widgets.add(Widgets.createSlot(new Point(startPoint.x + 24, startPoint.y - 24)).entries(recipeDisplay.getInputEntries().get(0))
							.disableBackground().markInput());
			if (size > 1) {
				widgets.add(
					Widgets.createSlot(new Point(startPoint.x + 24, startPoint.y + 24)).entries(recipeDisplay.getInputEntries().get(1))
						.disableBackground().markInput());
				if (size > 2) {
					widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y)).entries(recipeDisplay.getInputEntries().get(2))
									.disableBackground().markInput());
					if (size > 3) {
						widgets.add(
							Widgets.createSlot(new Point(startPoint.x + 48, startPoint.y)).entries(recipeDisplay.getInputEntries().get(3))
								.disableBackground().markInput());
						if (size > 4) {
							widgets.add(Widgets.createSlot(new Point(startPoint.x + 7, startPoint.y - 17))
											.entries(recipeDisplay.getInputEntries().get(4)).disableBackground().markInput());
							if (size > 5) {
								widgets.add(Widgets.createSlot(new Point(startPoint.x + 7, startPoint.y + 17))
												.entries(recipeDisplay.getInputEntries().get(5)).disableBackground().markInput());
								if (size > 6) {
									widgets.add(Widgets.createSlot(new Point(startPoint.x + 41, startPoint.y - 17))
													.entries(recipeDisplay.getInputEntries().get(6)).disableBackground().markInput());
									if (size > 7) {
										widgets.add(Widgets.createSlot(new Point(startPoint.x + 41, startPoint.y + 17))
														.entries(recipeDisplay.getInputEntries().get(7)).disableBackground().markInput());
									}
								}
							}
						}
					}
				}
			}
		}

		Label label = Widgets
			.createLabel(new Point(startPoint.x + 32, startPoint.y - 32), new TranslatableText(recipeDisplay.rite.getTranslationString()))
			.color(0xFF404040, 0xFFBBBBBB).noShadow().centered();
		if (recipeDisplay.getRite() instanceof TriggeredRite) {
			label.tooltip(I18n.translate("rei." + Constants.MOD_ID + ".tooltip.primable"));
		}
		widgets.add(label);

		int currentX = startPoint.x + 37 - recipeDisplay.rite.startConditions.length * 6;
		int currentY = startPoint.y + 46;
		for (RiteCondition condition : recipeDisplay.rite.startConditions) {
			widgets.add(new RiteConditionWidget(currentX, currentY, condition));
			currentX += 12;
		}
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 96;
	}

	@Override
	public CategoryIdentifier<? extends OctagramRiteCategory.OctagramDisplay> getCategoryIdentifier() {
		return MMREICompat.OCTAGRAM_RITE;
	}

	public static class OctagramDisplay implements Display {

		private final List<EntryIngredient> input;
		private final List<EntryIngredient> output;
		private final Rite rite;

		public OctagramDisplay(RiteRecipe recipe) {
			input = EntryIngredients.ofIngredients(recipe.ingredients);
			output = new ArrayList<>();
			rite = recipe.rite;
		}

		public Rite getRite() {
			return rite;
		}

		@Override
		public List<EntryIngredient> getInputEntries() {
			return input;
		}

		@Override
		public List<EntryIngredient> getOutputEntries() {
			return output;
		}

		@Override
		public CategoryIdentifier<?> getCategoryIdentifier() {
			return MMREICompat.OCTAGRAM_RITE;
		}
	}

	public static class RiteConditionWidget extends WidgetWithBounds {
		private final int x, y;
		private final Rectangle bounds;
		private final Tooltip tooltip;
		private final RiteCondition condition;

		public RiteConditionWidget(int x, int y, RiteCondition condition) {
			this.x = x;
			this.y = y;
			this.bounds = new Rectangle(x - 4, y - 4, 8, 8);
			this.condition = condition;
			this.tooltip = Tooltip.create(new Point(x - 4, y - 4), Util.trimText(condition.getDescription().getString()));
		}

		@Override
		public Rectangle getBounds() {
			return bounds;
		}

		@Override
		public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			matrices.push();
			matrices.translate(x + 0.5, y, 0);
			HudHandler.drawIcon(matrices, false, condition.getIconLocation());
			matrices.pop();
			if (isMouseOver(mouseX, mouseY)) {
				tooltip.queue();
			}
		}


		@Override
		public List<? extends Element> children() {
			return List.of();
		}
	}
}

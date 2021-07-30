package com.miskatonicmysteries.client.compat.rei.category;

import com.miskatonicmysteries.client.compat.rei.MMREICompat;
import com.miskatonicmysteries.common.feature.PotentialItem;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChemistrySetCategory implements DisplayCategory<ChemistrySetCategory.ChemistryDisplay> {
    public static final TranslatableText TITLE = new TranslatableText("rei.miskatonicmysteries.chemistry_recipe");
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "chemistry_recipe");
    public static final EntryStack<ItemStack> ICON = EntryStacks.of(MMObjects.CHEMISTRY_SET);

    @Override
    public Renderer getIcon() {
        return ICON;
    }

    @Override
    public Text getTitle() {
        return TITLE;
    }

    @Override
    public CategoryIdentifier<? extends ChemistryDisplay> getCategoryIdentifier() {
        return MMREICompat.CHEMISTRY;
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public @NotNull List<Widget> setupDisplay(ChemistryDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startPoint = new Point(bounds.getCenterX() - 64, bounds.getCenterY() - 2);
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y - 24)).entries(recipeDisplay.getInputEntries().get(0)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 22, startPoint.y - 24)).entries(recipeDisplay.getInputEntries().get(1)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y - 6)).entries(recipeDisplay.getInputEntries().get(2)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 22, startPoint.y - 6)).entries(recipeDisplay.getInputEntries().get(3)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 4, startPoint.y + 12)).entries(recipeDisplay.getInputEntries().get(4)).markInput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 22, startPoint.y + 12)).entries(recipeDisplay.getInputEntries().get(5)).markInput());
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 39, startPoint.y - 6)).animationDurationTicks(100));

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 66, startPoint.y - 6)).entries(recipeDisplay.getOutputEntries().get(0)).markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 88, startPoint.y - 6)).entries(recipeDisplay.getOutputMatches().get(0)).disableBackground().unmarkInputOrOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 66, startPoint.y - 24)).entries(recipeDisplay.getOutputEntries().get(1)).markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 88, startPoint.y - 24)).entries(recipeDisplay.getOutputMatches().get(1)).disableBackground().unmarkInputOrOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 66, startPoint.y + 12)).entries(recipeDisplay.getOutputEntries().get(2)).markOutput());
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 88, startPoint.y + 12)).entries(recipeDisplay.getOutputMatches().get(2)).disableBackground().unmarkInputOrOutput());

        return widgets;
    }

    public static class ChemistryDisplay implements Display {
        private final List<EntryIngredient> input;
        private final List<EntryIngredient> output;
        private final List<EntryIngredient> outputMatches;

        public ChemistryDisplay(ChemistryRecipe recipe) {
            input = EntryIngredients.ofIngredients(recipe.ingredients);
            output = new ArrayList<>();
            outputMatches = new ArrayList<>();
            for (PotentialItem potentialItem : recipe.output) {
                output.add(EntryIngredients.of(potentialItem.out));
                outputMatches.add(EntryIngredients.of(potentialItem.in));
            }
        }

        @Override
        public @NotNull List<EntryIngredient> getInputEntries() {
            return input;
        }

        @Override
        public List<EntryIngredient> getOutputEntries() {
            return output;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return MMREICompat.CHEMISTRY;
        }

        public List<EntryIngredient> getOutputMatches() {
            return outputMatches;
        }
    }
}

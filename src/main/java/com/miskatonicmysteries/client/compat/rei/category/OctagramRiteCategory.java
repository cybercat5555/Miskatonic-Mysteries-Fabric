package com.miskatonicmysteries.client.compat.rei.category;

import com.miskatonicmysteries.client.compat.rei.entry.SimpleOctagramEntry;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.block.OctagramBlock;
import com.miskatonicmysteries.common.feature.Affiliation;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
import com.miskatonicmysteries.common.feature.recipe.rite.TriggeredRite;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMObjects;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.RecipeDisplay;
import me.shedaniel.rei.api.widgets.Label;
import me.shedaniel.rei.api.widgets.Widgets;
import me.shedaniel.rei.gui.entries.RecipeEntry;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OctagramRiteCategory implements RecipeCategory<OctagramRiteCategory.Display> {
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "octagram_rite");
    public static final EntryStack LOGO = EntryStack.create(MMObjects.SHUB_CHALK);

    @Override
    public @NotNull Identifier getIdentifier() {
        return ID;
    }

    @Override
    public @NotNull EntryStack getLogo() {
        return LOGO;
    }

    @Override
    public int getDisplayHeight() {
        return 88;
    }

    @Override
    public @NotNull String getCategoryName() {
        return I18n.translate("rei." + ID.toString().replaceAll(":", "."));
    }

    @Override
    public @NotNull List<Widget> setupDisplay(Display recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startPoint = new Point(bounds.getCenterX() - 32, bounds.getCenterY() - 6);
        widgets.add(Widgets.createRecipeBase(bounds));
        SpriteIdentifier sprite = getMatchingOctagramTexture(recipeDisplay.getRite().octagramAffiliation);
        widgets.add(Widgets.createTexturedWidget(new Identifier(sprite.getTextureId().getNamespace(), "textures/" + sprite.getTextureId().getPath() + ".png"), startPoint.x, startPoint.y - 24, 0, 0, 64, 64, sprite.getSprite().getWidth(), sprite.getSprite().getHeight(), sprite.getSprite().getWidth(), sprite.getSprite().getHeight()));
        int size = recipeDisplay.getInputEntries().size();
        if (size > 0) { //peak elegance haha yes
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 24, startPoint.y - 24)).entries(recipeDisplay.getInputEntries().get(0)).disableBackground().markInput());
            if (size > 1) {
                widgets.add(Widgets.createSlot(new Point(startPoint.x + 24, startPoint.y + 24)).entries(recipeDisplay.getInputEntries().get(1)).disableBackground().markInput());
                if (size > 2) {
                    widgets.add(Widgets.createSlot(new Point(startPoint.x, startPoint.y)).entries(recipeDisplay.getInputEntries().get(2)).disableBackground().markInput());
                    if (size > 3) {
                        widgets.add(Widgets.createSlot(new Point(startPoint.x + 48, startPoint.y)).entries(recipeDisplay.getInputEntries().get(3)).disableBackground().markInput());
                        if (size > 4) {
                            widgets.add(Widgets.createSlot(new Point(startPoint.x + 7, startPoint.y - 17)).entries(recipeDisplay.getInputEntries().get(4)).disableBackground().markInput());
                            if (size > 5) {
                                widgets.add(Widgets.createSlot(new Point(startPoint.x + 7, startPoint.y + 17)).entries(recipeDisplay.getInputEntries().get(5)).disableBackground().markInput());
                                if (size > 6) {
                                    widgets.add(Widgets.createSlot(new Point(startPoint.x + 41, startPoint.y - 17)).entries(recipeDisplay.getInputEntries().get(6)).disableBackground().markInput());
                                    if (size > 7) {
                                        widgets.add(Widgets.createSlot(new Point(startPoint.x + 41, startPoint.y + 17)).entries(recipeDisplay.getInputEntries().get(7)).disableBackground().markInput());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Label label = Widgets.createLabel(new Point(startPoint.x + 32, startPoint.y - 32), new TranslatableText(recipeDisplay.rite.getTranslationString())).color(0xFF404040, 0xFFBBBBBB).noShadow().centered();
        if (recipeDisplay.getRite() instanceof TriggeredRite) {
            label.tooltipLine(I18n.translate("rei." + Constants.MOD_ID + ".tooltip.primable"));
        }
        widgets.add(label);
        return widgets;
    }

    private SpriteIdentifier getMatchingOctagramTexture(Affiliation affiliation) {
        if (affiliation == null || affiliation == Affiliation.NONE) {
            return ResourceHandler.DEFAULT_OCTAGRAM;
        }
        for (OctagramBlock octagramBlock : ResourceHandler.OCTAGRAM_SPRITES.keySet()) {
            if (affiliation.equals(octagramBlock.getAffiliation(false))) {
                return ResourceHandler.OCTAGRAM_SPRITES.get(octagramBlock);
            }
        }
        return ResourceHandler.DEFAULT_OCTAGRAM;
    }

    @Override
    public @NotNull RecipeEntry getSimpleRenderer(Display recipe) {
        return SimpleOctagramEntry.from(recipe::getInputEntries);
    }

    public static class Display implements RecipeDisplay {
        private final List<List<EntryStack>> input;
        private final List<List<EntryStack>> output;
        private final Rite rite;

        public Display(Rite recipe) {
            input = EntryStack.ofIngredients(recipe.ingredients);
            output = new ArrayList<>();
            rite = recipe;
        }

        public Rite getRite() {
            return rite;
        }

        @Override
        public @NotNull List<List<EntryStack>> getInputEntries() {
            return input;
        }

        @Override
        public @NotNull List<List<EntryStack>> getResultingEntries() {
            return output;
        }

        @Override
        public @NotNull Identifier getRecipeCategory() {
            return ID;
        }
    }
}

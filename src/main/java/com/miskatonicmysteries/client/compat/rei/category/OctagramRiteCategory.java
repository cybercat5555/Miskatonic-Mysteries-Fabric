package com.miskatonicmysteries.client.compat.rei.category;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.compat.rei.MMREICompat;
import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.recipe.rite.TriggeredRite;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.Constants;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Label;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OctagramRiteCategory implements DisplayCategory<OctagramRiteCategory.OctagramDisplay> {
    public static final TranslatableText TITLE = new TranslatableText("rei.miskatonicmysteries.octagram_rite");
    public static final Identifier ID = new Identifier(Constants.MOD_ID, "octagram_rite");
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
    public CategoryIdentifier<? extends OctagramRiteCategory.OctagramDisplay> getCategoryIdentifier() {
        return MMREICompat.OCTAGRAM_RITE;
    }


    @Override
    public int getDisplayHeight() {
        return 88;
    }

    @Override
    public @NotNull List<Widget> setupDisplay(OctagramDisplay recipeDisplay, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point startPoint = new Point(bounds.getCenterX() - 32, bounds.getCenterY() - 6);
        widgets.add(Widgets.createRecipeBase(bounds));
        SpriteIdentifier sprite = ResourceHandler.getMatchingOctagramTexture(recipeDisplay.getRite().getOctagramAffiliation());
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

  /*  @Override
    public DisplayRenderer getDisplayRenderer(OctagramDisplay display) {
        return SimpleOctagramEntry.from(display.input);
    }
*/

    public static class OctagramDisplay implements Display {
        private final List<EntryIngredient> input;
        private final List<EntryIngredient> output;
        private final Rite rite;

        public OctagramDisplay(Rite recipe) {
            input = EntryIngredients.ofIngredients(recipe.getIngredients());
            output = new ArrayList<>();
            rite = recipe;
        }

        public Rite getRite() {
            return rite;
        }

        @Override
        public List<EntryIngredient> getOutputEntries() {
            return output;
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return MMREICompat.OCTAGRAM_RITE;
        }

        @Override
        public List<EntryIngredient> getInputEntries() {
            return input;
        }
    }
}

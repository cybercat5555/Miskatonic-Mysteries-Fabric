package com.miskatonicmysteries.common.feature;

import com.miskatonicmysteries.lib.Util;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class ChemistryRecipe {
    public final List<Ingredient> INGREDIENTS = DefaultedList.ofSize(5, Ingredient.EMPTY);
    public final int COLOR;
    public final List<PotentialItem> OUTPUT = DefaultedList.ofSize(3, PotentialItem.EMPTY);

    public ChemistryRecipe(Ingredient[] ingredients, int color, PotentialItem... output) {
        for (int i = 0; i < ingredients.length; i++) {
            INGREDIENTS.set(i, ingredients[i]);
        }
        for (int i = 0; i < output.length; i++) {
            OUTPUT.set(i, output[i]);
        }
        COLOR = color;
    }
}

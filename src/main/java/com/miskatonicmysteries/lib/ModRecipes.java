package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.lib.util.MiscUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class ModRecipes {
  // private static final Map<Identifier, ChemistryRecipe> CHEMISTRY_RECIPES = new ConcurrentHashMap<>();
    public static final DummyRecipeType<ChemistryRecipe> CHEMISTRY_RECIPE = new DummyRecipeType<>();
    public static final RecipeSerializer<ChemistryRecipe> CHEMISTRY_SERIALIZER = new ChemistryRecipe.Serializer();
    public static void init() {
        MiscUtil.register(Registry.RECIPE_TYPE, "chemistry_recipe", CHEMISTRY_RECIPE);
        MiscUtil.register(Registry.RECIPE_SERIALIZER, "chemistry_recipe", CHEMISTRY_SERIALIZER);
    }

    public static ChemistryRecipe getRecipe(BlockEntityChemistrySet chemSet){
        return chemSet.getWorld().getRecipeManager().listAllOfType(CHEMISTRY_RECIPE).stream().filter(r -> MiscUtil.areItemStackListsExactlyEqual(r.ingredients, chemSet)).findFirst().orElse(null);
    }

    private static class DummyRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(this)).toString();
        }
    }
}

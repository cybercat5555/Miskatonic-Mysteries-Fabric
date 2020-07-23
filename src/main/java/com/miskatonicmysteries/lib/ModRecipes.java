package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.feature.PotentialItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ModRecipes {
  // private static final Map<Identifier, ChemistryRecipe> CHEMISTRY_RECIPES = new ConcurrentHashMap<>();
    public static final DummyRecipeType<ChemistryRecipe> CHEMISTRY_RECIPE = new DummyRecipeType<>();
    public static final RecipeSerializer<ChemistryRecipe> CHEMISTRY_SERIALIZER = new ChemistryRecipe.Serializer();
    public static void init(){
        Util.register(Registry.RECIPE_TYPE, "chemistry_recipe", CHEMISTRY_RECIPE);
        Util.register(Registry.RECIPE_SERIALIZER, "chemistry_recipe", CHEMISTRY_SERIALIZER);

      /*  addChemistryRecipe(new ChemistryRecipe(new Identifier(Constants.MOD_ID, "laudanum"),
                new Ingredient[]{Ingredient.ofItems(Items.POPPY), Ingredient.ofItems(Items.POPPY), Ingredient.ofItems(Items.SUGAR),  Ingredient.ofItems(Items.SUGAR),  Ingredient.ofItems(Items.WHEAT)}, 16740608,
                new PotentialItem(new ItemStack(Items.GLASS_BOTTLE), new ItemStack(ModObjects.LAUDANUM)), new PotentialItem(new ItemStack(Items.GLASS_BOTTLE), new ItemStack(ModObjects.LAUDANUM))));
        ///recipes will be data-driven oop/*/

    }

    public static ChemistryRecipe getRecipe(BlockEntityChemistrySet chemSet){
        return chemSet.getWorld().getRecipeManager().method_30027(CHEMISTRY_RECIPE).stream().filter(r -> Util.areItemStackListsExactlyEqual(r.INGREDIENTS, chemSet)).findFirst().orElse(null);
    }

    private static class DummyRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(this)).toString();
        }
    }
}

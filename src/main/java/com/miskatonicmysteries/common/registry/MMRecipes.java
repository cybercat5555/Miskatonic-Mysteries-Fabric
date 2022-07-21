package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.feature.block.blockentity.ChemistrySetBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;
import com.miskatonicmysteries.common.util.InventoryUtil;
import com.miskatonicmysteries.common.util.RegistryUtil;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

import java.util.Objects;
import java.util.stream.Collectors;

public class MMRecipes {

	public static final DummyRecipeType<ChemistryRecipe> CHEMISTRY_RECIPE = new DummyRecipeType<>();
	public static final RecipeSerializer<ChemistryRecipe> CHEMISTRY_SERIALIZER = new ChemistryRecipe.Serializer();

	public static final DummyRecipeType<RiteRecipe> RITE_RECIPE = new DummyRecipeType<>();
	public static final RecipeSerializer<RiteRecipe> RITE_SERIALIZER = new RiteRecipe.Serializer();

	public static void init() {
		RegistryUtil.register(Registry.RECIPE_TYPE, "chemistry_recipe", CHEMISTRY_RECIPE);
		RegistryUtil.register(Registry.RECIPE_SERIALIZER, "chemistry_recipe", CHEMISTRY_SERIALIZER);
		RegistryUtil.register(Registry.RECIPE_TYPE, "rite_recipe", RITE_RECIPE);
		RegistryUtil.register(Registry.RECIPE_SERIALIZER, "rite_recipe", RITE_SERIALIZER);
	}

	public static ChemistryRecipe getChemistryRecipe(ChemistrySetBlockEntity chemSet) {
		return chemSet.getWorld().getRecipeManager().listAllOfType(CHEMISTRY_RECIPE).stream()
			.filter(r -> InventoryUtil.areItemStackListsExactlyEqual(r.ingredients, chemSet)).findFirst().orElse(null);
	}

	public static RiteRecipe getRiteRecipe(OctagramBlockEntity octagram) {
		return octagram.getWorld().getRecipeManager().listAllOfType(RITE_RECIPE).stream()
			.filter(r -> r.matchExactly ? InventoryUtil.areItemStackListsExactlyEqual(r.ingredients, octagram) :
						 InventoryUtil.containsAllIngredients(r.ingredients.stream().filter(
						 	ingredient -> !ingredient.isEmpty()).collect(Collectors.toList()), octagram.getItems()))
			.findFirst().orElse(null);
	}

	private static class DummyRecipeType<T extends Recipe<?>> implements RecipeType<T> {

		@Override
		public String toString() {
			return Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(this)).toString();
		}
	}
}

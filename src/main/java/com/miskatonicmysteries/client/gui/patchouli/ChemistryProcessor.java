package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.registry.MMRecipes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ChemistryProcessor implements IComponentProcessor {

	protected ChemistryRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		String recipeId = variables.get("recipe").asString();
		RecipeManager manager = MinecraftClient.getInstance().world.getRecipeManager();
		recipe = (ChemistryRecipe) manager.get(new Identifier(recipeId))
			.filter(recipe -> recipe.getType().equals(MMRecipes.CHEMISTRY_RECIPE)).orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(String key) {
		for (int i = 0; i < recipe.ingredients.size(); i++) {
			if (key.equals("ingredient" + (i + 1))) {
				ItemStack[] stack = recipe.ingredients.get(i).getMatchingStacks(); //patchouli pls
				return IVariable.from(stack);
			}
		}
		for (int i = 0; i < recipe.output.size(); i++) {
			if (key.equals("output_in" + (i + 1))) {
				ItemStack stack = recipe.output.get(i).in;
				return IVariable.from(stack);
			}
		}
		for (int i = 0; i < recipe.output.size(); i++) {
			if (key.equals("output_out" + (i + 1))) {
				ItemStack stack = recipe.output.get(i).out;
				return IVariable.from(stack);
			}
		}
		return null;
	}
}

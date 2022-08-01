package com.miskatonicmysteries.client.gui.patchouli;

import com.miskatonicmysteries.client.render.ResourceHandler;
import com.miskatonicmysteries.common.feature.recipe.RiteRecipe;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RiteProcessor implements IComponentProcessor {

	protected RiteRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		this.recipe = (RiteRecipe) MinecraftClient.getInstance().world.getRecipeManager().get(new Identifier(variables.get("rite").asString()))
			.orElseThrow(IllegalArgumentException::new);
	}

	@Override
	public IVariable process(String key) {
		switch (key) {
			case "octagram": {
				SpriteIdentifier sprite = ResourceHandler.getMatchingOctagramTexture(recipe.rite.getOctagramAffiliation());
				return IVariable.wrap(
					new Identifier(sprite.getTextureId().getNamespace(), "textures/" + sprite.getTextureId().getPath() + ".png")
						.toString());
			}
			case "rite_name":
				return IVariable.wrap(I18n.translate(recipe.rite.getTranslationString()));
			case "rite_id":
				return IVariable.wrap(recipe.rite.getId().toString());
			default: {
				for (int i = 0; i < recipe.getIngredients().size(); i++) {
					if (key.equals("ingredient" + (i + 1))) {
						ItemStack[] stacks = recipe.getIngredients().get(i).getMatchingStacks();
						return IVariable.from(stacks);
					}
				}
			}
		}
		return null;
	}
}

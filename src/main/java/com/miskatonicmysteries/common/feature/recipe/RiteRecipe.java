package com.miskatonicmysteries.common.feature.recipe;

import com.miskatonicmysteries.api.interfaces.LazySerializable;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.registry.MMRecipes;
import com.miskatonicmysteries.common.registry.MMRegistries;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RiteRecipe implements LazySerializable {

	public final DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(8, Ingredient.EMPTY);
	public final boolean matchExactly;
	public final Rite rite;
	public final Identifier id;

	public RiteRecipe(Identifier id, Ingredient[] ingredients, boolean matchExactly, Rite rite) {
		this.id = id;
		for (int i = 0; i < ingredients.length; i++) {
			this.ingredients.set(i, ingredients[i]);
		}
		this.rite = rite;
		this.matchExactly = matchExactly;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return MMRecipes.RITE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return MMRecipes.RITE_RECIPE;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return ingredients;
	}

	public static class Serializer implements RecipeSerializer<RiteRecipe> {

		@Override
		public RiteRecipe read(Identifier id, JsonObject json) {
			Ingredient[] ingredients = readIngredients(JsonHelper.getArray(json, "ingredients"));
			Rite rite = MMRegistries.RITES.get(new Identifier(JsonHelper.getString(json, "rite")));
			return new RiteRecipe(id, ingredients, JsonHelper.getBoolean(json, "match_exactly", true), rite);
		}

		private Ingredient[] readIngredients(JsonArray json) {
			List<Ingredient> ingredients = new ArrayList<>();
			json.forEach(jsonElement -> ingredients.add(Ingredient.fromJson(jsonElement)));
			return ingredients.toArray(new Ingredient[ingredients.size()]);
		}

		@Override
		public RiteRecipe read(Identifier id, PacketByteBuf buf) {
			int sizeIngredients = buf.readInt();
			List<Ingredient> ingredients = new ArrayList<>();
			for (int i = 0; i < sizeIngredients; i++) {
				ingredients.add(Ingredient.fromPacket(buf));
			}
			Rite rite = MMRegistries.RITES.get(buf.readIdentifier());
			return new RiteRecipe(id, ingredients.toArray(new Ingredient[ingredients.size()]), buf.readBoolean(), rite);
		}

		@Override
		public void write(PacketByteBuf buf, RiteRecipe recipe) {
			buf.writeInt(recipe.ingredients.size());
			recipe.ingredients.forEach(i -> i.write(buf));
			buf.writeIdentifier(recipe.rite.getId());
			buf.writeBoolean(recipe.matchExactly);
		}
	}
}

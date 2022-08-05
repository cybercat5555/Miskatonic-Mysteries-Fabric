package com.miskatonicmysteries.mixin.recipe;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {

	@Invoker("readSymbols")
	static Map<String, Ingredient> invokeReadSymbols(JsonObject json) {
		throw new AssertionError();
	}

	@Invoker("removePadding")
	static String[] invokeRemovePadding(String ... pattern) {
		throw new AssertionError();
	}

	@Invoker("getPattern")
	static String[] invokeGetPattern(JsonArray json) {
		throw new AssertionError();
	}

	@Invoker("createPatternMatrix")
	static DefaultedList<Ingredient> invokeCreatePatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
		throw new AssertionError();
	}
}

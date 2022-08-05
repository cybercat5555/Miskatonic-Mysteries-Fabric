package com.miskatonicmysteries.common.feature.recipe;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.interfaces.PlayerProvider;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.mixin.recipe.ShapedRecipeAccessor;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Map;

import com.google.gson.JsonObject;

public class AscensionLockedRecipe extends ShapedRecipe {
	final int stage;
	final Affiliation affiliation;
	public AscensionLockedRecipe(Identifier id, String group, int width, int height,
								 DefaultedList<Ingredient> input,
								 ItemStack output, int stage, Affiliation affiliation) {
		super(id, group, width, height, input, output);
		this.stage = stage;
		this.affiliation = affiliation;
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		if (craftingInventory instanceof PlayerProvider p && p.mm_getPlayer() != null) {
			if (affiliation != MMAffiliations.NONE && MiskatonicMysteriesAPI.getNonNullAffiliation(p.mm_getPlayer(), false) != affiliation) {
				return false;
			}
			if (MiskatonicMysteriesAPI.getAscensionStage(p.mm_getPlayer()) < stage) {
				return false;
			}
		} else {
			return false;
		}
		return super.matches(craftingInventory, world);
	}

	public static class Serializer implements RecipeSerializer<AscensionLockedRecipe> {

		@Override
		public AscensionLockedRecipe read(Identifier identifier, JsonObject jsonObject) {
			String string = JsonHelper.getString(jsonObject, "group", "");
			Map<String, Ingredient> map = ShapedRecipeAccessor.invokeReadSymbols(JsonHelper.getObject(jsonObject, "key"));
			String[] strings = ShapedRecipeAccessor.invokeRemovePadding(
				ShapedRecipeAccessor.invokeGetPattern(JsonHelper.getArray(jsonObject, "pattern")));
			int i = strings[0].length();
			int j = strings.length;
			DefaultedList<Ingredient> defaultedList = ShapedRecipeAccessor.invokeCreatePatternMatrix(strings, map, i, j);
			ItemStack itemStack = ShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
			int stage = JsonHelper.getInt(jsonObject, "stage", 0);
			Affiliation affiliation = MMRegistries.AFFILIATIONS.get(
				new Identifier(JsonHelper.getString(jsonObject, "affiliation", MMAffiliations.NONE.getId().toString())));
			return new AscensionLockedRecipe(identifier, string, i, j, defaultedList, itemStack, stage, affiliation);
		}

		@Override
		public AscensionLockedRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			int i = packetByteBuf.readVarInt();
			int j = packetByteBuf.readVarInt();
			String string = packetByteBuf.readString();
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);
			for (int k = 0; k < defaultedList.size(); ++k) {
				defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
			}
			ItemStack itemStack = packetByteBuf.readItemStack();
			int stage = packetByteBuf.readInt();
			Affiliation affiliation = MMRegistries.AFFILIATIONS.get(packetByteBuf.readIdentifier());
			return new AscensionLockedRecipe(identifier, string, i, j, defaultedList, itemStack, stage, affiliation);
		}

		@Override
		public void write(PacketByteBuf packetByteBuf, AscensionLockedRecipe shapedRecipe) {
			packetByteBuf.writeVarInt(shapedRecipe.getWidth());
			packetByteBuf.writeVarInt(shapedRecipe.getHeight());
			packetByteBuf.writeString(shapedRecipe.getGroup());
			for (Ingredient ingredient : shapedRecipe.getIngredients()) {
				ingredient.write(packetByteBuf);
			}
			packetByteBuf.writeItemStack(shapedRecipe.getOutput());
			packetByteBuf.writeInt(shapedRecipe.stage);
			packetByteBuf.writeIdentifier(shapedRecipe.affiliation.getId());
		}
	}
}

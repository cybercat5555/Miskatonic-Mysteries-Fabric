package com.miskatonicmysteries.common.feature.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.miskatonicmysteries.common.feature.PotentialItem;
import com.miskatonicmysteries.lib.ModRecipes;
import com.miskatonicmysteries.lib.Util;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ChemistryRecipe implements LazySerializable{
    public final List<Ingredient> INGREDIENTS = DefaultedList.ofSize(5, Ingredient.EMPTY);
    public final int COLOR;
    public final List<PotentialItem> OUTPUT = DefaultedList.ofSize(3, PotentialItem.EMPTY);
    public final Identifier ID;
    public ChemistryRecipe(Identifier id, Ingredient[] ingredients, int color, PotentialItem... output) {
        this.ID = id;
        for (int i = 0; i < ingredients.length; i++) {
            INGREDIENTS.set(i, ingredients[i]);
        }
        for (int i = 0; i < output.length; i++) {
            OUTPUT.set(i, output[i]);
        }
        COLOR = color;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CHEMISTRY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CHEMISTRY_RECIPE;
    }

    public static class Serializer implements RecipeSerializer<ChemistryRecipe> {

        @Override
        public ChemistryRecipe read(Identifier id, JsonObject json) {
            Ingredient[] ingredients = readIngredients(JsonHelper.getArray(json, "ingredients"));
            PotentialItem[] outputs = readOutputs(JsonHelper.getArray(json, "output"));
            return new ChemistryRecipe(id, ingredients, JsonHelper.getInt(json, "color", 0), outputs);
        }

        private PotentialItem[] readOutputs(JsonArray json) {
            ArrayList<PotentialItem> outputs = new ArrayList<>();
            json.forEach(jsonElement -> {
                outputs.add(PotentialItem.fromJson((JsonObject) jsonElement));
            });
            return outputs.toArray(new PotentialItem[outputs.size()]);
        }

        private Ingredient[] readIngredients(JsonArray json) {
            List<Ingredient> ingredients = new ArrayList<>();
            json.forEach(jsonElement -> ingredients.add(Ingredient.fromJson(jsonElement)));
            return ingredients.toArray(new Ingredient[ingredients.size()]);
        }

        @Override
        public ChemistryRecipe read(Identifier id, PacketByteBuf buf) {
            int sizeIngredients = buf.readInt();
            int sizeOutput = buf.readInt();
            List<Ingredient> ingredients = new ArrayList<>();
            List<PotentialItem> outputs = new ArrayList<>();
            for (int i = 0; i < sizeIngredients; i++)
                ingredients.add(Ingredient.fromPacket(buf));
            for (int i = 0; i < sizeOutput; i++)
                outputs.add(PotentialItem.fromPacket(buf));

            return new ChemistryRecipe(id, ingredients.toArray(new Ingredient[ingredients.size()]), buf.readInt(), outputs.toArray(new PotentialItem[outputs.size()]));
        }

        @Override
        public void write(PacketByteBuf buf, ChemistryRecipe recipe) {
            buf.writeInt(recipe.INGREDIENTS.size());
            buf.writeInt(recipe.OUTPUT.size());
            recipe.INGREDIENTS.forEach(i -> i.write(buf));
            recipe.OUTPUT.forEach(p -> p.write(buf));
            buf.writeInt(recipe.COLOR);
        }
    }
}

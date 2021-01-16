package com.miskatonicmysteries.common.lib;

import com.miskatonicmysteries.common.block.blockentity.ChemistrySetBlockEntity;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
import com.miskatonicmysteries.common.feature.recipe.rite.TeleportRite;
import com.miskatonicmysteries.common.lib.util.InventoryUtil;
import com.miskatonicmysteries.common.lib.util.RegistryUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class ModRecipes {
    public static final DummyRecipeType<ChemistryRecipe> CHEMISTRY_RECIPE = new DummyRecipeType<>();
    public static final RecipeSerializer<ChemistryRecipe> CHEMISTRY_SERIALIZER = new ChemistryRecipe.Serializer();

    public static final Rite TELEPORT_RITE = new TeleportRite();


    public static void init() {
        RegistryUtil.register(Registry.RECIPE_TYPE, "chemistry_recipe", CHEMISTRY_RECIPE);
        RegistryUtil.register(Registry.RECIPE_SERIALIZER, "chemistry_recipe", CHEMISTRY_SERIALIZER);

        addRite(TELEPORT_RITE);
    }

    public static Rite getRite(OctagramBlockEntity octagram) {//todo use recipe manager when function command/data driven rites are a thing (compare insanity effects)
        return Rite.RITES.values().stream().filter(r -> r.canCast(octagram)).findFirst().orElse(null);
    }

    public static ChemistryRecipe getRecipe(ChemistrySetBlockEntity chemSet) {
        return chemSet.getWorld().getRecipeManager().listAllOfType(CHEMISTRY_RECIPE).stream().filter(r -> InventoryUtil.areItemStackListsExactlyEqual(r.ingredients, chemSet)).findFirst().orElse(null);
    }

    public static Rite addRite(Rite rite) {
        return Rite.RITES.put(rite.id, rite);
    }

    private static class DummyRecipeType<T extends Recipe<?>> implements RecipeType<T> {
        @Override
        public String toString() {
            return Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(this)).toString();
        }
    }
}

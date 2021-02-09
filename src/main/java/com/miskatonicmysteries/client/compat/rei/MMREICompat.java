package com.miskatonicmysteries.client.compat.rei;

import com.miskatonicmysteries.client.compat.rei.category.ChemistrySetCategory;
import com.miskatonicmysteries.client.compat.rei.category.OctagramRiteCategory;
import com.miskatonicmysteries.common.item.ChalkItem;
import com.miskatonicmysteries.common.registry.MMRecipes;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MMREICompat implements REIPluginV0 {
    private static final Identifier ID = new Identifier(Constants.MOD_ID, "rei");

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategory(new ChemistrySetCategory());
        recipeHelper.registerCategory(new OctagramRiteCategory());
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        World world = MinecraftClient.getInstance().world;
        if (world != null) {
            world.getRecipeManager().listAllOfType(MMRecipes.CHEMISTRY_RECIPE).forEach(recipe -> recipeHelper.registerDisplay(new ChemistrySetCategory.Display(recipe)));
        }
        MMRegistries.RITES.forEach((rite) -> recipeHelper.registerDisplay(new OctagramRiteCategory.Display(rite)));
    }

    @Override
    public Identifier getPluginIdentifier() {
        return ID;
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {

        recipeHelper.registerWorkingStations(ChemistrySetCategory.ID, ChemistrySetCategory.LOGO);
        List<EntryStack> validChalks = new ArrayList<>();
        Registry.ITEM.forEach(item -> {
            if (item instanceof ChalkItem) {
                validChalks.add(EntryStack.create(item));
            }
        });
        recipeHelper.registerWorkingStations(OctagramRiteCategory.ID, validChalks.toArray(new EntryStack[0]));
    }
}

package com.miskatonicmysteries.client.compat.rei;

import com.miskatonicmysteries.client.compat.rei.category.ChemistrySetCategory;
import com.miskatonicmysteries.client.compat.rei.category.OctagramRiteCategory;
import com.miskatonicmysteries.common.feature.recipe.rite.Rite;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMRecipes;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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
        Rite.RITES.forEach((id, rite) -> {
            recipeHelper.registerDisplay(new OctagramRiteCategory.Display(rite));
        });
    }

    @Override
    public Identifier getPluginIdentifier() {
        return ID;
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(ChemistrySetCategory.ID, ChemistrySetCategory.LOGO);
        recipeHelper.registerWorkingStations(OctagramRiteCategory.ID, OctagramRiteCategory.LOGO);
    }
}

package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.feature.ChemistryRecipe;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModRecipes {
    public static final Map<Identifier, ChemistryRecipe> CHEMISTRY_RECIPES = new ConcurrentHashMap<>();

    public static void init(){

    }

    public static ChemistryRecipe getRecipe(BlockEntityChemistrySet chemSet){
        return CHEMISTRY_RECIPES.values().stream().filter(r -> Util.areItemStackListsExactlyEqual(r.INGREDIENTS, chemSet)).findFirst().orElse(null);
    }
}

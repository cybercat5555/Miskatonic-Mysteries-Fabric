package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

public class ModRegistries {
    public static final SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));

    public static void init(){
        ModCommand.setup();
        Util.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);
    }
}

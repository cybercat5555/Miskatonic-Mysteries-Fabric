package com.miskatonicmysteries.client.compat.rei;

import com.miskatonicmysteries.api.item.ChalkItem;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.client.compat.rei.category.ChemistrySetCategory;
import com.miskatonicmysteries.client.compat.rei.category.OctagramRiteCategory;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.util.Constants;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.display.DynamicDisplayGenerator;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MMREICompat implements REIClientPlugin {
    public static final CategoryIdentifier<ChemistrySetCategory.ChemistryDisplay> CHEMISTRY = CategoryIdentifier.of(new Identifier(Constants.MOD_ID, "chemistry"));
    public static final CategoryIdentifier<OctagramRiteCategory.OctagramDisplay> OCTAGRAM_RITE = CategoryIdentifier.of(new Identifier(Constants.MOD_ID, "octagram_rite"));


    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ChemistrySetCategory());
        registry.addWorkstations(CHEMISTRY, ChemistrySetCategory.ICON);
        registry.removePlusButton(CHEMISTRY);
        registry.add(new OctagramRiteCategory());
        List<EntryStack<ItemStack>> validChalks = new ArrayList<>();
        Registry.ITEM.forEach(item -> {
            if (item instanceof ChalkItem) {
                validChalks.add(EntryStacks.of(item));
            }
        });
        registry.addWorkstations(OCTAGRAM_RITE,  validChalks.toArray(new EntryStack[0]));
        registry.removePlusButton(OCTAGRAM_RITE);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(ChemistryRecipe.class, ChemistrySetCategory.ChemistryDisplay::new);
        registry.registerFiller(Rite.class, OctagramRiteCategory.OctagramDisplay::new); //todo since this is not a recipe, it is not filled properly
    }
}

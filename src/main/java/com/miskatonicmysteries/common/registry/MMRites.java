package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.rite.*;
import com.miskatonicmysteries.common.feature.recipe.rite.summon.PrinceSummoningRite;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class MMRites {
    public static final Rite OPEN_WAY = new TeleportRite();
    public static final Rite BURNED_VEIL = new BurnedVeilRite();
    public static final Rite BROKEN_VEIL = new BrokenVeilRite();
    public static final Rite HYSTERIA = new HysteriaRite();
    public static final Rite SCULPTOR_RITE = new SculptorRite();
    public static final Rite GOLDEN_FLOCK_RITE = new GoldenFlockRite();
    public static final Rite SUMMON_PRINCE_RITE = new PrinceSummoningRite();

    public static final Rite REGENERATION_SPELL_RITE = new SpellGivingRite(MMSpellEffects.HEAL, Constants.Misc.WITCH_KNOWLEDGE, new Identifier(Constants.MOD_ID, "regeneration"), null, 0,
            Ingredient.ofItems(Items.GLISTERING_MELON_SLICE), Ingredient.ofItems(MMObjects.OCEANIC_GOLD), Ingredient.ofItems(Items.SPONGE), Ingredient.ofItems(Items.GOLDEN_APPLE), Ingredient.ofItems(Items.GHAST_TEAR));
    public static final Rite RESISTANCE_SPELL_RITE = new SpellGivingRite(MMSpellEffects.RESISTANCE, Constants.Misc.WITCH_KNOWLEDGE, new Identifier(Constants.MOD_ID, "resistance"), null, 0,
            Ingredient.ofItems(Items.SCUTE), Ingredient.ofItems(MMObjects.OCEANIC_GOLD), Ingredient.ofItems(Items.IRON_INGOT), Ingredient.ofItems(Items.BEEF), Ingredient.ofItems(Items.QUARTZ), Ingredient.ofItems(Items.DIAMOND));
    public static final Rite KNOCKBACK_SPELL_RITE = new SpellGivingRite(MMSpellEffects.KNOCKBACK, "", new Identifier(Constants.MOD_ID, "knockback"), null, 0,
            Ingredient.ofItems(Items.FEATHER), Ingredient.ofItems(MMObjects.OCEANIC_GOLD), Ingredient.ofStacks(Arrays.stream(new ItemStack[]{PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.STRONG_SWIFTNESS)})), Ingredient.ofItems(Items.BEEF), Ingredient.ofItems(Items.CHICKEN), Ingredient.ofItems(Items.ARROW));

    public static void init() {
        register(OPEN_WAY);
        register(BURNED_VEIL);
        register(BROKEN_VEIL);
        register(HYSTERIA);
        register(SCULPTOR_RITE);
        register(GOLDEN_FLOCK_RITE);
        register(SUMMON_PRINCE_RITE);

        register(REGENERATION_SPELL_RITE);
        register(RESISTANCE_SPELL_RITE);
        register(KNOCKBACK_SPELL_RITE);
    }

    private static void register(Rite rite) {
        Registry.register(MMRegistries.RITES, rite.getId(), rite);
    }

    public static Rite getRite(OctagramBlockEntity octagram) {
        return MMRegistries.RITES.stream().filter(r -> r.canCast(octagram)).findFirst().orElse(null);
    }
}

package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.feature.effect.*;
import com.miskatonicmysteries.common.util.RegistryUtil;
import com.miskatonicmysteries.mixin.BrewingRecipeRegistryAccessor;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.util.registry.Registry;

public class MMStatusEffects {
    public static final StatusEffect MANIA = new ManiaStatusEffect();
    public static final StatusEffect TRANQUILIZED = new TranquilizedStatusEffect();
    public static final StatusEffect OVERMEDICATED = new OvermedicalizedStatusEffect();
    public static final StatusEffect LAZARUS = new LazarusStatusEffect();
    public static final StatusEffect BLEED = new BleedStatusEffect();
    public static final StatusEffect RESONANCE = new ResonanceStatusEffect();
    public static final StatusEffect EXOTIC_CRAVINGS = new ResonanceStatusEffect();
    public static final StatusEffect ULTRA_VIOLENCE = new UltraViolenceStatusEffect();

    static class Potions {
        public static final Potion RESONANCE = new Potion(new StatusEffectInstance(MMStatusEffects.RESONANCE, 3600, 0));
        public static final Potion LONG_RESONANCE = new Potion(new StatusEffectInstance(MMStatusEffects.RESONANCE, 9600, 0));
        public static final Potion STRONG_RESONANCE = new Potion(new StatusEffectInstance(MMStatusEffects.RESONANCE, 1800, 1));
    }

    public static void init() {
        RegistryUtil.register(Registry.STATUS_EFFECT, "mania", MANIA);
        RegistryUtil.register(Registry.STATUS_EFFECT, "tranquilized", TRANQUILIZED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "overmedicated", OVERMEDICATED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "lazarus", LAZARUS);
        RegistryUtil.register(Registry.STATUS_EFFECT, "bleed", BLEED);
        RegistryUtil.register(Registry.STATUS_EFFECT, "resonance", RESONANCE);
        RegistryUtil.register(Registry.STATUS_EFFECT, "exotic_cravings", EXOTIC_CRAVINGS);
        RegistryUtil.register(Registry.STATUS_EFFECT, "ultra_violence", ULTRA_VIOLENCE);

        RegistryUtil.register(Registry.POTION, "resonance", MMStatusEffects.Potions.RESONANCE);
        RegistryUtil.register(Registry.POTION, "resonance_long", MMStatusEffects.Potions.LONG_RESONANCE);
        RegistryUtil.register(Registry.POTION, "resonance_strong", MMStatusEffects.Potions.STRONG_RESONANCE);

        BrewingRecipeRegistryAccessor.invokeRegister(net.minecraft.potion.Potions.WATER, MMObjects.RESONATE_OOZE, MMStatusEffects.Potions.RESONANCE);
        BrewingRecipeRegistryAccessor.invokeRegister(MMStatusEffects.Potions.RESONANCE, Items.REDSTONE, MMStatusEffects.Potions.LONG_RESONANCE);
        BrewingRecipeRegistryAccessor.invokeRegister(MMStatusEffects.Potions.RESONANCE, Items.GLOWSTONE_DUST, MMStatusEffects.Potions.STRONG_RESONANCE);
    }
}

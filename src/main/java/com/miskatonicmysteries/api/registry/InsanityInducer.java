package com.miskatonicmysteries.api.registry;

import com.google.gson.JsonObject;
import com.miskatonicmysteries.api.interfaces.DataSerializable;
import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.registry.MMRegistries;
import net.minecraft.entity.LivingEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class InsanityInducer implements DataSerializable<InsanityInducer> {
    public final Identifier id;
    public final int sanityPenalty;
    public final boolean decreasesSanityCap;
    public final boolean withShockFactor;
    public final Ingredient ingredient;
    public final boolean ignoreFactors;

    public InsanityInducer(Identifier id, int sanityPenalty, boolean decreasesSanityCap, boolean withShockFactor, boolean ignoreFactors, Ingredient ingredient) {
        this.id = id;
        this.sanityPenalty = sanityPenalty;
        this.decreasesSanityCap = decreasesSanityCap;
        this.withShockFactor = withShockFactor;
        this.ignoreFactors = ignoreFactors;
        this.ingredient = ingredient;
    }

    public void induceInsanity(World world, LivingEntity entity, Sanity sanity) {
        if (!world.isClient) {
            sanity.setSanity(sanity.getSanity() - sanityPenalty, ignoreFactors);
            if (decreasesSanityCap) sanity.addSanityCapExpansion(id.toString(), -sanityPenalty);
            if (withShockFactor) sanity.setShocked(true);
            sanity.syncSanityData();
        }
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public static class Serializer extends DataSerializable.DataReader<InsanityInducer> {
        @Override
        public InsanityInducer readFromJson(Identifier id, JsonObject json) {
            return new InsanityInducer(id, JsonHelper.getInt(json, "sanityPenalty"), JsonHelper.getBoolean(json, "decreasesSanityCap", false), JsonHelper.getBoolean(json, "withShock", false), JsonHelper.getBoolean(json, "ignoreFactors", false), Ingredient.fromJson(JsonHelper.getObject(json, "inducer")));
        }

        @Override
        public Registry<InsanityInducer> getRegistry() {
            return MMRegistries.INSANITY_INDUCERS;
        }
    }
}

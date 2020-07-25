package com.miskatonicmysteries.common.feature.sanity;

import com.google.gson.JsonObject;
import com.miskatonicmysteries.common.feature.DataSerializable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class InsanityInducer implements DataSerializable<InsanityInducer> {
    public static final Map<Identifier, InsanityInducer> INSANITY_INDUCERS = new HashMap<>();
    public final Identifier id;
    public final int sanityPenalty;
    public final boolean decreasesSanityCap;
    public final boolean withShockFactor;
    public final Ingredient ingredient;

    public InsanityInducer(Identifier id, int sanityPenalty, boolean decreasesSanityCap, boolean withShockFactor, Ingredient ingredient) {
        this.id = id;
        this.sanityPenalty = sanityPenalty;
        this.decreasesSanityCap = decreasesSanityCap;
        this.withShockFactor = withShockFactor;
        this.ingredient = ingredient;
    }

    public void induceInsanity(World world, LivingEntity entity, ISanity sanity){
        if (!world.isClient){
            sanity.setSanity(sanity.getSanity() - sanityPenalty);
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
            return new InsanityInducer(id, JsonHelper.getInt(json, "sanityPenalty"), JsonHelper.getBoolean(json, "decreasesSanityCap", false), JsonHelper.getBoolean(json, "withShock", false), Ingredient.fromJson(JsonHelper.getObject(json, "inducer")));
        }

        @Override
        public Map<Identifier, InsanityInducer> getAccessMap() {
            return INSANITY_INDUCERS;
        }
    }
}

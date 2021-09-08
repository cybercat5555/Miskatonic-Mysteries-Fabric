package com.miskatonicmysteries.common.feature.criterion;

import com.google.gson.JsonObject;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class RiteCastCriterion extends AbstractCriterion<RiteCastCriterion.Conditions> {
    private static final Identifier ID = new Identifier(Constants.MOD_ID, "rite_cast");

    public Identifier getId() {
        return ID;
    }

    public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) throws NullPointerException {
        Identifier riteId = new Identifier(JsonHelper.getString(jsonObject, "rite"));
        return new Conditions(extended, MMRegistries.RITES.get(riteId));
    }

    public void trigger(ServerPlayerEntity player, Rite rite) {
        this.test(player, (conditions) -> conditions.matches(rite));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final Rite rite;

        public Conditions(EntityPredicate.Extended player, Rite item) {
            super(RiteCastCriterion.ID, player);
            this.rite = item;
        }

        public boolean matches(Rite rite) {
            return this.rite.getId().equals(rite.getId());
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.addProperty("rite", this.rite.getId().toString());
            return jsonObject;
        }
    }
}

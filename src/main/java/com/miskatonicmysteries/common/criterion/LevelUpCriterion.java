package com.miskatonicmysteries.common.criterion;

import com.google.gson.JsonObject;
import com.miskatonicmysteries.api.registry.Affiliation;
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

public class LevelUpCriterion extends AbstractCriterion<LevelUpCriterion.Conditions> {
    private static final Identifier ID = new Identifier(Constants.MOD_ID, "level_up");

    public Identifier getId() {
        return ID;
    }

    public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) throws NullPointerException {
        Identifier affiliationId = new Identifier(JsonHelper.getString(jsonObject, "affiliation"));
        int stage = JsonHelper.getInt(jsonObject, "stage", -1);
        return new Conditions(extended, MMRegistries.AFFILIATIONS.get(affiliationId), stage);
    }

    public void trigger(ServerPlayerEntity player, Affiliation affiliation, int stage) {
        this.test(player, (conditions) -> conditions.matches(affiliation, stage));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final Affiliation affiliation;
        private final int stage;

        public Conditions(EntityPredicate.Extended player, Affiliation affiliation, int stage) {
            super(LevelUpCriterion.ID, player);
            this.affiliation = affiliation;
            this.stage = stage;
        }

        public boolean matches(Affiliation affiliation, int stage) {
            return stage >= this.stage && this.affiliation.equals(affiliation);
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.addProperty("affiliation", this.affiliation.getId().toString());
            jsonObject.addProperty("stage", stage);
            return jsonObject;
        }
    }
}

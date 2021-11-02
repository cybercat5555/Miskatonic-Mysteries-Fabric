package com.miskatonicmysteries.common.feature.criterion;

import com.google.gson.JsonObject;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class KnowledgeCriterion extends AbstractCriterion<KnowledgeCriterion.Conditions> {

	private static final Identifier ID = new Identifier(Constants.MOD_ID, "knowledge");

	public Identifier getId() {
		return ID;
	}

	public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended,
		AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) throws NullPointerException {
		String knowledge = (JsonHelper.getString(jsonObject, "knowledge"));
		return new Conditions(extended, knowledge);
	}

	public void trigger(ServerPlayerEntity player, String knowledge) {
		this.test(player, (conditions) -> conditions.matches(knowledge));
	}

	public static class Conditions extends AbstractCriterionConditions {

		private final String knowledge;

		public Conditions(EntityPredicate.Extended player, String knowledge) {
			super(KnowledgeCriterion.ID, player);
			this.knowledge = knowledge;
		}

		public boolean matches(String knowledge) {
			return this.knowledge.equals(knowledge);
		}

		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.addProperty("knowledge", this.knowledge);
			return jsonObject;
		}
	}
}

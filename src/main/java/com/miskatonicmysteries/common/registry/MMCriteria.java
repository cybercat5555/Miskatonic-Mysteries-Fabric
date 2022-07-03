package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.ConfigurablePredicateType;
import com.miskatonicmysteries.common.feature.criterion.KnowledgeCriterion;
import com.miskatonicmysteries.common.feature.criterion.LevelUpCriterion;
import com.miskatonicmysteries.common.feature.criterion.RiteCastCriterion;
import com.miskatonicmysteries.common.handler.predicate.ConfigurableTargetPredicate;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;

public class MMCriteria {

	public static final ConfigurablePredicateType TARGET_PREDICATE = new ConfigurablePredicateType(
		new Identifier(Constants.MOD_ID, "players"), ConfigurableTargetPredicate::new,
		ConfigurableTargetPredicate.AFFILIATION_CATEGORY,
		ConfigurableTargetPredicate.PLAYER_CATEGORY,
		ConfigurableTargetPredicate.MISC_CATEGORY);

	public static final RiteCastCriterion RITE_CAST = new RiteCastCriterion();
	public static final LevelUpCriterion LEVEL_UP = new LevelUpCriterion();
	public static final KnowledgeCriterion KNOWLEDGE = new KnowledgeCriterion();

	public static void init() {
		RegistryUtil.register(MMRegistries.CONFIGURABLE_PREDICATES, "players", TARGET_PREDICATE);

		Criteria.register(RITE_CAST);
		Criteria.register(LEVEL_UP);
		Criteria.register(KNOWLEDGE);
	}
}

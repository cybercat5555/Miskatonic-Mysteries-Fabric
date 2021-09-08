package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.feature.criterion.KnowledgeCriterion;
import com.miskatonicmysteries.common.feature.criterion.LevelUpCriterion;
import com.miskatonicmysteries.common.feature.criterion.RiteCastCriterion;
import net.fabricmc.fabric.api.object.builder.v1.advancement.CriterionRegistry;

public class MMCriteria {
    public static final RiteCastCriterion RITE_CAST = new RiteCastCriterion();
    public static final LevelUpCriterion LEVEL_UP = new LevelUpCriterion();
    public static final KnowledgeCriterion KNOWLEDGE = new KnowledgeCriterion();

    public static void init() {
        CriterionRegistry.register(RITE_CAST);
        CriterionRegistry.register(LEVEL_UP);
        CriterionRegistry.register(KNOWLEDGE);
    }
}

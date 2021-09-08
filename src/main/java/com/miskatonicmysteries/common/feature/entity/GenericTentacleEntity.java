package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class GenericTentacleEntity extends TentacleEntity {
    public GenericTentacleEntity(EntityType<? extends GenericTentacleEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public Affiliation getAffiliation(boolean apparent) {
        return MMAffiliations.NONE;
    }
}

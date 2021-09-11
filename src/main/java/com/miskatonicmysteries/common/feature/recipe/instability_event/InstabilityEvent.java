package com.miskatonicmysteries.common.feature.recipe.instability_event;

import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import net.minecraft.util.Identifier;

public class InstabilityEvent {
    private Identifier id;
    private float instabilityThreshold, baseChance;

    public InstabilityEvent(Identifier id, float instabilityThreshold, float baseChance) {
        this.id = id;
        this.instabilityThreshold = instabilityThreshold;
        this.baseChance = baseChance;
    }

    public boolean shouldCast(OctagramBlockEntity blockEntity, float instability) {
        return instability >= instabilityThreshold && blockEntity.getWorld().random.nextFloat() < baseChance;
    }

    /**
     * @param blockEntity
     * @param instability
     * @return if the event ends the current rite of the octagram
     */
    public boolean cast(OctagramBlockEntity blockEntity, float instability) {
        return false;
    }

    public Identifier getId() {
        return id;
    }
}

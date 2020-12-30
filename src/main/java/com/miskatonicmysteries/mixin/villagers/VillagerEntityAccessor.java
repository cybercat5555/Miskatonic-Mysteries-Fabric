package com.miskatonicmysteries.mixin.villagers;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.dynamic.GlobalPos;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.function.BiPredicate;

@Mixin(VillagerEntity.class)
public interface VillagerEntityAccessor {
    @Accessor("POINTS_OF_INTEREST")
    static void setPointsOfInterest(Map<MemoryModuleType<GlobalPos>, BiPredicate<VillagerEntity, PointOfInterestType>> map) {
        throw new UnsupportedOperationException();
    }
}

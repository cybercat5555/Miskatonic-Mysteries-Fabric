package com.miskatonicmysteries.common.registry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.miskatonicmysteries.mixin.entity.PointOfInterestTypesAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Set;

public class MMPoi {
    public static final RegistryKey<PointOfInterestType> HASTUR = of("hastur");

    private static final Set<BlockState> HASTUR_POI = ImmutableList.of(
            MMObjects.HASTUR_OCTAGRAM, MMObjects.HASTUR_STATUE_STONE, MMObjects.HASTUR_STATUE_TERRACOTTA, MMObjects.HASTUR_STATUE_GOLD,
            MMObjects.HASTUR_STATUE_MOSSY, MMObjects.MOSSY_HASTUR_MURAL, MMObjects.STONE_HASTUR_MURAL, MMObjects.TERRACOTTA_HASTUR_MURAL,
            MMObjects.YELLOW_TERRACOTTA_HASTUR_MURAL
            ).stream().flatMap((block) -> block.getStateManager().getStates().stream()).collect(ImmutableSet.toImmutableSet());


    private static RegistryKey<PointOfInterestType> of(String id) {
        return RegistryKey.of(Registry.POINT_OF_INTEREST_TYPE_KEY, new Identifier(id));
    }

    public static void init() {
        PointOfInterestTypesAccessor.callRegister(Registry.POINT_OF_INTEREST_TYPE,
                HASTUR,
                HASTUR_POI,
                1,
                1);

    }
}

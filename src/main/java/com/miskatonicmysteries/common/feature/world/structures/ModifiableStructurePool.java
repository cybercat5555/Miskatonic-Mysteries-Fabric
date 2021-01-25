package com.miskatonicmysteries.common.feature.world.structures;

import com.miskatonicmysteries.mixin.world.StructurePoolAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;

import java.util.List;

//credit goes to Foundationgames and Draylar
public class ModifiableStructurePool {
    private final StructurePool pool;

    public ModifiableStructurePool(StructurePool pool) {
        this.pool = pool;
    }

    public void addStructurePoolElement(StructurePoolElement element) {
        addStructurePoolElement(element, 1);
    }

    public void addStructurePoolElement(StructurePoolElement element, int weight) {
        for (int i = 0; i < weight; i++) {
            ((StructurePoolAccessor) pool).getElements().add(element);
        }
        ((StructurePoolAccessor) pool).getElementCounts().add(Pair.of(element, weight));
    }

    public List<StructurePoolElement> getElements() {
        return ((StructurePoolAccessor) pool).getElements();
    }

    public StructurePool getStructurePool() {
        return pool;
    }
}

package com.miskatonicmysteries.common.feature.world.structures;

import com.miskatonicmysteries.common.mixin.StructurePoolAccessor;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;

//code taken from https://github.com/FoundationGames/Sandwichable/tree/master/remappedSrc/io/github/foundationgames/sandwichable (credit goes to Foundationgames and Draylar)
public class ModifiableStructurePool {
    private final StructurePool pool;

    public ModifiableStructurePool(StructurePool pool) {
        this.pool = pool;
    }

    public void addStructurePoolElement(StructurePoolElement element) {
        ((StructurePoolAccessor) pool).getElements().add(element);
    }

    public void addStructurePoolElement(StructurePoolElement element, int weight) {
        for (int i = 0; i < weight; i++) {
            addStructurePoolElement(element);
        }
    }

    public StructurePool getStructurePool() {
        return pool;
    }
}

package com.miskatonicmysteries.common.lib.util;

import com.miskatonicmysteries.common.feature.world.structures.ModifiableStructurePool;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

public class WorldUtil {
    public static StructurePool tryAddElementToPool(Identifier targetPool, StructurePool pool, String elementId, StructurePool.Projection projection, int weight, StructureProcessorList processorList) {
        // System.out.println(targetPool);
        if (targetPool.equals(pool.getId())) {
            //  System.out.println("adding to pool: " + elementId);
            ModifiableStructurePool modPool = new ModifiableStructurePool(pool);
            modPool.addStructurePoolElement(StructurePoolElement.method_30426(elementId, processorList).apply(projection), weight);
            return modPool.getStructurePool();
        }
        return pool;
    }
}

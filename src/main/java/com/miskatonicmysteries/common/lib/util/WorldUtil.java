package com.miskatonicmysteries.common.lib.util;

import com.miskatonicmysteries.common.handler.callback.StructurePoolRegistryCallback;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;

public class WorldUtil {
    public static void addStructureToPool(StructurePool.Projection projection, Identifier poolID, Identifier elementID, int weight, StructureProcessorList processors) {
        StructurePoolRegistryCallback.EVENT.register(structurePool -> {
            if (structurePool.getStructurePool().getId().equals(poolID)) {
                structurePool.addStructurePoolElement(StructurePoolElement.method_30426(elementID.toString(), processors).apply(StructurePool.Projection.RIGID), weight);//new SinglePoolElement(elementID.toString(), processors), weight);
            }
        });
    }
}

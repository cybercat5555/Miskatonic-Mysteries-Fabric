package com.miskatonicmysteries.lib.util;

import com.miskatonicmysteries.common.handler.callback.StructurePoolRegistryCallback;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.util.Identifier;

import java.util.List;

public class WorldUtil {
    public static void addStructureToPool(Identifier poolID, Identifier elementID, int weight, List<StructureProcessor> processors) {
        StructurePoolRegistryCallback.EVENT.register(structurePool -> {
            if (structurePool.getStructurePool().getId().equals(poolID)) {
                structurePool.addStructurePoolElement(new SinglePoolElement(elementID.toString(), processors), weight);
            }
        });
    }
}

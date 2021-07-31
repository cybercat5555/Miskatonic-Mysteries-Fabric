package com.miskatonicmysteries.common.util;

import com.miskatonicmysteries.common.feature.world.structures.ModifiableStructurePool;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegistryUtil {
    public static Block registerBlock(Block block, String name) {
        Block registeredBlock = register(Registry.BLOCK, name, block);
        register(Registry.ITEM, name, new BlockItem(block, new Item.Settings().group(Constants.MM_GROUP)));
        return registeredBlock;
    }

    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Constants.MOD_ID, name), entry);
    }

    public static StructurePool tryAddElementToPool(Identifier targetPool, StructurePool pool, String elementId, StructurePool.Projection projection, int weight, StructureProcessorList processorList) {
        if (targetPool.equals(pool.getId())) {
            ModifiableStructurePool modPool = new ModifiableStructurePool(pool);
            modPool.addStructurePoolElement(StructurePoolElement.ofProcessedLegacySingle(elementId, processorList).apply(projection), weight);
            return modPool.getStructurePool();
        }
        return pool;
    }
}

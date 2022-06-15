package com.miskatonicmysteries.common.util;

import com.miskatonicmysteries.mixin.world.StructurePoolAccessor;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;

import com.mojang.datafixers.util.Pair;

public class RegistryUtil {

	public static Block registerBlock(Block block, String name) {
		Block registeredBlock = register(Registry.BLOCK, name, block);
		register(Registry.ITEM, name, new BlockItem(block, new Item.Settings().group(Constants.MM_GROUP)));
		return registeredBlock;
	}

	public static <T> T register(Registry<? super T> registry, String name, T entry) {
		return Registry.register(registry, new Identifier(Constants.MOD_ID, name), entry);
	}

	public static void tryAddElementToPool(Identifier targetPool, StructurePool pool, String elementId, StructurePool.Projection projection,
										   int weight) {
		tryAddElementToPool(targetPool, pool, elementId, projection, weight, StructureProcessorLists.EMPTY);
	}

	public static void tryAddElementToPool(Identifier targetPool, StructurePool pool, String elementId, StructurePool.Projection projection,
										   int weight, RegistryEntry<StructureProcessorList> processors) {
		if (targetPool.equals(pool.getId())) {
			StructurePoolElement element = StructurePoolElement.ofProcessedLegacySingle(elementId, processors).apply(projection);
			for (int i = 0; i < weight; i++) {
				((StructurePoolAccessor) pool).getElements().add(element);
			}
			((StructurePoolAccessor) pool).getElementCounts().add(Pair.of(element, weight));
		}
	}
}

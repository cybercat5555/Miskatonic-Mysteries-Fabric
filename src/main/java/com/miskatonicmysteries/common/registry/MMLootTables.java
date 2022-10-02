package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.util.Constants;


import net.minecraft.loot.LootTables;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MMLootTables {

	protected static final Identifier TRANQ_TABLE = new Identifier(Constants.MOD_ID, "injects/tranquilizer");
	protected static final Identifier OCEANIC_GOLD_TABLE = new Identifier(Constants.MOD_ID, "injects/oceanic_gold");
	protected static final Identifier MM_FISHING_TABLE = new Identifier(Constants.MOD_ID, "injects/mm_fishing");
	protected static final Identifier INCANTATION_TABLE = new Identifier(Constants.MOD_ID, "injects/incantation");
	protected static final Identifier IDOL_TABLE = new Identifier(Constants.MOD_ID, "injects/idol");
	protected static final Identifier BOOK_TABLE = new Identifier(Constants.MOD_ID, "injects/necronomicon");
	protected static final Map<Identifier, List<Identifier>> LOOT_TABLE_INJECTS = new HashMap<>();

	public static void init() {
		LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_BUTCHER_CHEST, List.of(TRANQ_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_CARTOGRAPHER_CHEST, List.of(TRANQ_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_TANNERY_CHEST, List.of(TRANQ_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_PLAINS_CHEST, List.of(TRANQ_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.BURIED_TREASURE_CHEST, List.of(OCEANIC_GOLD_TABLE, BOOK_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.SHIPWRECK_TREASURE_CHEST, List.of(OCEANIC_GOLD_TABLE, BOOK_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.UNDERWATER_RUIN_BIG_CHEST, List.of(OCEANIC_GOLD_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.UNDERWATER_RUIN_SMALL_CHEST, List.of(OCEANIC_GOLD_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.STRONGHOLD_CROSSING_CHEST, List.of(OCEANIC_GOLD_TABLE, IDOL_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.STRONGHOLD_CORRIDOR_CHEST, List.of(IDOL_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.STRONGHOLD_LIBRARY_CHEST, List.of(INCANTATION_TABLE, IDOL_TABLE, BOOK_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.SIMPLE_DUNGEON_CHEST, List.of(INCANTATION_TABLE, BOOK_TABLE, OCEANIC_GOLD_TABLE));
		LOOT_TABLE_INJECTS.put(LootTables.IGLOO_CHEST_CHEST, List.of(INCANTATION_TABLE));

		/*TODO
		LootTableEvents.MODIFY.register((resourceManager, lootManager, identifier, builder, lootTableSetter) -> {
			if (LOOT_TABLE_INJECTS.containsKey(identifier)) {
				List<FabricLootSupplier> tables = LOOT_TABLE_INJECTS.get(identifier).stream().map(id -> (FabricLootSupplier) FabricLootSupplierBuilder
					.of(lootManager.getTable(id)).build()).collect(Collectors.toList());
				for (FabricLootSupplier table : tables) {
					builder.withPools(table.getPools());
				}
				lootTableSetter.set(builder.build());
			}
			if (identifier.equals(LootTables.FISHING_TREASURE_GAMEPLAY)){
				LootPool pool = ((FabricLootSupplier) lootManager.getTable(identifier)).getPools().get(0);
				FabricLootPool mmPool = (FabricLootPool) ((FabricLootSupplier) lootManager.getTable(MM_FISHING_TABLE)).getPools().get(0);
				FabricLootSupplierBuilder newBuilder = FabricLootSupplierBuilder.builder();
				if (pool != null && mmPool != null) {
					FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder().copyFrom(pool);
					for (LootPoolEntry entry : mmPool.getEntries()) {
						poolBuilder.withEntry(entry);
					}
					newBuilder.withPool(poolBuilder.build());
					newBuilder.type(LootContextTypes.FISHING);
					lootTableSetter.set(newBuilder.build());
				}
			}
		});

		 */

	}
}

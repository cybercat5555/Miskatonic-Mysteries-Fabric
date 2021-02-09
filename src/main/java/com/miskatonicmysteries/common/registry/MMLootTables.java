package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class MMLootTables {
    public static final Identifier TRANQ_TABLE = new Identifier(Constants.MOD_ID, "injects/tranquilizer");
    public static final Identifier OCEANIC_GOLD_TABLE = new Identifier(Constants.MOD_ID, "injects/oceanic_gold");
    public static final Identifier INCANTATION_TABLE = new Identifier(Constants.MOD_ID, "injects/incantation");
    public static final Map<Identifier, Identifier> LOOT_TABLE_INJECTS = new HashMap<>();

    public static void init() {
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_BUTCHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_CARTOGRAPHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_TANNERY_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.VILLAGE_PLAINS_CHEST, TRANQ_TABLE);

        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.BURIED_TREASURE_CHEST, OCEANIC_GOLD_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.SHIPWRECK_TREASURE_CHEST, OCEANIC_GOLD_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.UNDERWATER_RUIN_BIG_CHEST, OCEANIC_GOLD_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.UNDERWATER_RUIN_SMALL_CHEST, OCEANIC_GOLD_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.STRONGHOLD_CROSSING_CHEST, OCEANIC_GOLD_TABLE);

        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.STRONGHOLD_LIBRARY_CHEST, INCANTATION_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.SIMPLE_DUNGEON_CHEST, INCANTATION_TABLE);
        LOOT_TABLE_INJECTS.put(net.minecraft.loot.LootTables.IGLOO_CHEST_CHEST, INCANTATION_TABLE);
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, builder, lootTableSetter) -> {
            if (LOOT_TABLE_INJECTS.containsKey(identifier)) {
                FabricLootSupplier lootSupplier = (FabricLootSupplier) FabricLootSupplierBuilder.of(lootManager.getTable(LOOT_TABLE_INJECTS.get(identifier))).build();
                lootTableSetter.set(builder.withPools(lootSupplier.getPools()).build());
            }
        });
    }
}

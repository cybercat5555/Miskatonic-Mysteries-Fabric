package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.feature.effect.StatusEffectMania;
import com.miskatonicmysteries.common.feature.effect.StatusEffectOvermedicated;
import com.miskatonicmysteries.common.feature.effect.StatusEffectTranquilized;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.loot.LootTables;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class ModRegistries {
    public static final SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));

    public static final StatusEffect MANIA = new StatusEffectMania();
    public static final StatusEffect TRANQUILIZED = new StatusEffectTranquilized();

    public static final Identifier INFESTED_WHEAT_LOOT_TABLE = new Identifier(Constants.MOD_ID, "injects/infested_wheat");
    public static final Identifier TALLOW_LOOT_TABLE = new Identifier(Constants.MOD_ID, "injects/tallow");
    public static final Identifier TRANQ_TABLE = new Identifier(Constants.MOD_ID, "injects/tranquilizer");

    public static final Map<Identifier, Identifier> LOOT_TABLE_INJECTS = new HashMap<>();
    public static final StatusEffect OVERMEDICATED = new StatusEffectOvermedicated();


    public static void init() {
        initLootTableEdits();
        ModCommand.setup();
        Util.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);

        Util.register(Registry.STATUS_EFFECT, "mania", MANIA);
        Util.register(Registry.STATUS_EFFECT, "tranquilized", TRANQUILIZED);
        Util.register(Registry.STATUS_EFFECT, "overmedicated", OVERMEDICATED);
    }

    private static void initLootTableEdits() {
        LOOT_TABLE_INJECTS.put(Blocks.WHEAT.getLootTableId(), INFESTED_WHEAT_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.PIG.getLootTableId(), TALLOW_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.VILLAGER.getLootTableId(), TALLOW_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.PLAYER.getLootTableId(), TALLOW_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.PIGLIN.getLootTableId(), TALLOW_LOOT_TABLE);

        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_BUTCHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_CARTOGRAPHER_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_TANNERY_CHEST, TRANQ_TABLE);
        LOOT_TABLE_INJECTS.put(LootTables.VILLAGE_PLAINS_CHEST, TRANQ_TABLE);

        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, builder, lootTableSetter) -> {
            if (LOOT_TABLE_INJECTS.containsKey(identifier)) {
                FabricLootSupplier lootSupplier = (FabricLootSupplier) FabricLootSupplierBuilder.of(lootManager.getTable(LOOT_TABLE_INJECTS.get(identifier))).build();
                lootTableSetter.set(builder.withPools(lootSupplier.getPools()).build());
            }
        });
    }
}

package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.feature.ModCommand;
import com.miskatonicmysteries.common.feature.recipe.ChemistryRecipe;
import com.miskatonicmysteries.common.feature.sanity.ISanity;
import com.miskatonicmysteries.common.feature.sanity.InsanityEvent;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplier;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ModRegistries {
    public static final SoundEvent GUN_SHOT = new SoundEvent(new Identifier(Constants.MOD_ID, "gun_shot"));

    public static final Identifier INFESTED_WHEAT_LOOT_TABLE = new Identifier(Constants.MOD_ID, "injects/infested_wheat");
    public static final Identifier TALLOW_LOOT_TABLE = new Identifier(Constants.MOD_ID, "injects/tallow");

    public static final Map<Identifier, Identifier> LOOT_TABLE_INJECTS = new HashMap<>();

    public static void init(){
        initLootTableEdits();
        ModCommand.setup();
        Util.register(Registry.SOUND_EVENT, "gun_shot", GUN_SHOT);
    }

    private static void initLootTableEdits() {
        LOOT_TABLE_INJECTS.put(Blocks.WHEAT.getLootTableId(), INFESTED_WHEAT_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.PIG.getLootTableId(), TALLOW_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.VILLAGER.getLootTableId(), TALLOW_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.PLAYER.getLootTableId(), TALLOW_LOOT_TABLE);
        LOOT_TABLE_INJECTS.put(EntityType.PIGLIN.getLootTableId(), TALLOW_LOOT_TABLE);
        LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, identifier, builder, lootTableSetter) -> {
            if (LOOT_TABLE_INJECTS.containsKey(identifier)){
                FabricLootSupplier lootSupplier = (FabricLootSupplier) FabricLootSupplierBuilder.of(lootManager.getTable(LOOT_TABLE_INJECTS.get(identifier))).build();
                lootTableSetter.set(builder.withPools(lootSupplier.getPools()).build());
            }
        });
    }
}

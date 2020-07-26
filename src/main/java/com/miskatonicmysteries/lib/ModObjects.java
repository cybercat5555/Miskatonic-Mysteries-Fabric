package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.BlockChemistrySet;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.item.ItemRevolver;
import com.miskatonicmysteries.common.item.ItemRifle;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ModObjects {
    public static final Block CHEMISTRY_SET = new BlockChemistrySet();


    public static final Block OCEANIC_GOLD_BLOCK = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
    public static final Block WARDED_OCEANIC_GOLD_BLOCK = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD.strength(2.5F, 12));
    public static final Block OCEANIC_GOLD_TILES = new Block(Constants.BlockSettings.OCEANIC_GOLD);

    public static final Block OCEANIC_GOLD_PILLAR = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
    public static final Block OCEANIC_GOLD_PILLAR_ORNATE = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
    public static final Block OCEANIC_GOLD_PILLAR_SPLIT = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);

    public static final Item OCEANIC_GOLD = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item RIFLE = new ItemRifle();
    public static final Item REVOLVER = new ItemRevolver();
    public static final Item BULLET = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item INFESTED_WHEAT = new Item(new Item.Settings().group(Constants.MM_GROUP));
    //all these have no effect yet, but need one
    public static final Item BLOTTER = new Item(new Item.Settings().group(Constants.MM_GROUP));
    public static final Item LAUDANUM = new Item(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item RE_AGENT_SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(SYRINGE));

    public static final Item TALLOW = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final BlockEntityType<BlockEntityChemistrySet> CHEMISTRY_SET_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityChemistrySet::new, CHEMISTRY_SET).build(null);

    public static void init(){
        Util.register(Registry.BLOCK_ENTITY_TYPE, "chemistry_set", CHEMISTRY_SET_BLOCK_ENTITY_TYPE);
        Util.registerBlock(CHEMISTRY_SET, "chemistry_set");

        Util.registerBlock(OCEANIC_GOLD_BLOCK, "oceanic_gold_block");
        Util.registerBlock(WARDED_OCEANIC_GOLD_BLOCK, "oceanic_gold_block_warded");
        Util.registerBlock(OCEANIC_GOLD_TILES, "oceanic_gold_tiles");
        Util.registerBlock(OCEANIC_GOLD_PILLAR, "oceanic_gold_pillar");
        Util.registerBlock(OCEANIC_GOLD_PILLAR_ORNATE, "oceanic_gold_pillar_ornate");
        Util.registerBlock(OCEANIC_GOLD_PILLAR_SPLIT, "oceanic_gold_pillar_split");

        Util.register(Registry.ITEM, "oceanic_gold", OCEANIC_GOLD);

        Util.register(Registry.ITEM, "rifle", RIFLE);
        Util.register(Registry.ITEM, "revolver", REVOLVER);
        Util.register(Registry.ITEM, "bullet", BULLET);

        Util.register(Registry.ITEM, "syringe", SYRINGE);
        Util.register(Registry.ITEM, "infested_wheat", INFESTED_WHEAT);
        Util.register(Registry.ITEM, "blotter", BLOTTER);
        Util.register(Registry.ITEM, "laudanum", LAUDANUM);
        Util.register(Registry.ITEM, "re_agent_syringe", RE_AGENT_SYRINGE);

        Util.register(Registry.ITEM, "tallow", TALLOW);
    }
}

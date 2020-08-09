package com.miskatonicmysteries.lib;

import com.miskatonicmysteries.common.block.*;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityAltar;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityChemistrySet;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityOctagram;
import com.miskatonicmysteries.common.block.blockentity.BlockEntityStatue;
import com.miskatonicmysteries.common.item.*;
import com.miskatonicmysteries.common.item.books.ItemMMBook;
import com.miskatonicmysteries.lib.util.Constants;
import com.miskatonicmysteries.lib.util.Util;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Block CHEMISTRY_SET = new BlockChemistrySet();
    public static final BlockEntityType<BlockEntityChemistrySet> CHEMISTRY_SET_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityChemistrySet::new, CHEMISTRY_SET).build(null);

    public static final Block BLACKSTONE_ALTAR = new BlockAltar(true, AbstractBlock.Settings.copy(Blocks.BLACKSTONE).lightLevel(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
    public static final Block CORAL_ALTAR = new BlockAltar(true, AbstractBlock.Settings.copy(Blocks.DEAD_BRAIN_CORAL_BLOCK).lightLevel(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
    public static final Block FUNKY_ALTAR = new BlockAltar(false, AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK).lightLevel(state -> 12));
    public static final Block MOSSY_STONE_ALTAR = new BlockAltar(true, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE).lightLevel(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
    public static final Block NETHER_BRICK_ALTAR = new BlockAltar(true, AbstractBlock.Settings.copy(Blocks.NETHER_BRICKS).lightLevel(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
    public static final Block PRISMARINE_ALTAR = new BlockAltar(false, AbstractBlock.Settings.copy(Blocks.PRISMARINE).lightLevel(state -> 8));
    public static final Block SANDSTONE_ALTAR = new BlockAltar(true, AbstractBlock.Settings.copy(Blocks.SANDSTONE).lightLevel(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
    public static final Block STONE_ALTAR = new BlockAltar(true, AbstractBlock.Settings.copy(Blocks.STONE).lightLevel(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
    public static final BlockEntityType<BlockEntityAltar> ALTAR_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityAltar::new, BlockAltar.ALTARS.toArray(new BlockAltar[BlockAltar.ALTARS.size()])).build(null);

    public static final Block OCTAGRAM_SIDES = new BlockOctagram.BlockOuterOctagram();
    public static final BlockOctagram CTHULHU_OCTAGRAM = new BlockOctagram(Constants.Affiliation.CTHULHU);
    public static final BlockOctagram HASTUR_OCTAGRAM = new BlockOctagram(Constants.Affiliation.HASTUR);
    public static final BlockOctagram SHUB_OCTAGRAM = new BlockOctagram(Constants.Affiliation.SHUB);
    public static final BlockEntityType<BlockEntityOctagram> OCTAGRAM_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityOctagram::new, BlockOctagram.OCTAGRAMS.toArray(new BlockOctagram[BlockOctagram.OCTAGRAMS.size()])).build(null);

    public static final BlockStatue CTHULHU_STATUE_GOLD = new BlockStatue(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
    public static final BlockStatue CTHULHU_STATUE_MOSSY = new BlockStatue(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
    public static final BlockStatue CTHULHU_STATUE_PRISMARINE = new BlockStatue(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.PRISMARINE));
    public static final BlockStatue CTHULHU_STATUE_STONE = new BlockStatue(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.STONE));

    public static final BlockStatue HASTUR_STATUE_GOLD = new BlockStatue(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
    public static final BlockStatue HASTUR_STATUE_MOSSY = new BlockStatue(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
    public static final BlockStatue HASTUR_STATUE_TERRACOTTA = new BlockStatue(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.TERRACOTTA));
    public static final BlockStatue HASTUR_STATUE_STONE = new BlockStatue(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.STONE));

    public static final BlockStatue SHUB_STATUE_GOLD = new BlockStatue(Constants.Affiliation.SHUB, AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
    public static final BlockStatue SHUB_STATUE_MOSSY = new BlockStatue(Constants.Affiliation.SHUB, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
    public static final BlockStatue SHUB_STATUE_BLACKSTONE = new BlockStatue(Constants.Affiliation.SHUB, AbstractBlock.Settings.copy(Blocks.BLACKSTONE));
    public static final BlockStatue SHUB_STATUE_STONE = new BlockStatue(Constants.Affiliation.SHUB, AbstractBlock.Settings.copy(Blocks.STONE));
    public static final BlockEntityType<BlockEntityStatue> STATUE_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityStatue::new, BlockStatue.STATUES.toArray(new BlockStatue[BlockStatue.STATUES.size()])).build(null);

    public static final Block STONE_CTHULHU_MURAL = new BlockMural(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block MOSSY_CTHULHU_MURAL = new BlockMural(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
    public static final Block CORAL_CTHULHU_MURAL = new BlockMural(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.DEAD_BRAIN_CORAL_BLOCK));
    public static final Block PRISMARINE_CTHULHU_MURAL = new BlockMural(Constants.Affiliation.CTHULHU, AbstractBlock.Settings.copy(Blocks.PRISMARINE));

    public static final Block STONE_HASTUR_MURAL = new BlockMural(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block MOSSY_HASTUR_MURAL = new BlockMural(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
    public static final Block TERRACOTTA_HASTUR_MURAL = new BlockMural(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.TERRACOTTA));
    public static final Block YELLOW_TERRACOTTA_HASTUR_MURAL = new BlockMural(Constants.Affiliation.HASTUR, AbstractBlock.Settings.copy(Blocks.YELLOW_TERRACOTTA));

    public static final Block STONE_SHUB_MURAL = new BlockMural(Constants.Affiliation.SHUB, AbstractBlock.Settings.copy(Blocks.STONE));
    public static final Block MOSSY_SHUB_MURAL = new BlockMural(Constants.Affiliation.SHUB, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));

    public static final Block OCEANIC_GOLD_BLOCK = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
    public static final Block WARDED_OCEANIC_GOLD_BLOCK = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD.strength(2.5F, 12));
    public static final Block OCEANIC_GOLD_TILES = new Block(Constants.BlockSettings.OCEANIC_GOLD);

    public static final Block OCEANIC_GOLD_PILLAR = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
    public static final Block OCEANIC_GOLD_PILLAR_ORNATE = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
    public static final Block OCEANIC_GOLD_PILLAR_SPLIT = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);

    public static final Block CANDLE = new BlockCandle();

    public static final Block DUMMY_RESONATOR_ON = new Block(AbstractBlock.Settings.copy(Blocks.GLASS));
    public static final Block DUMMY_RESONATOR_OFF = new Block(AbstractBlock.Settings.copy(Blocks.GLASS));
    public static final Block POWERCELL = new Block(AbstractBlock.Settings.copy(Blocks.GLASS));

    public static final ItemMMBook SCIENCE_JOURNAL = new ItemMMBook(new Identifier(Constants.MOD_ID, "science_journal"), Constants.Affiliation.NONE, false);
    public static final ItemMMBook NECRONOMICON = new ItemMMBook(new Identifier(Constants.MOD_ID, "necronomicon"), Constants.Affiliation.NONE, true);

    public static final Item OCEANIC_GOLD = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item RIFLE = new ItemRifle();
    public static final Item REVOLVER = new ItemRevolver();
    public static final Item BULLET = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item INFESTED_WHEAT = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item BLOTTER = new ItemBlotter();
    public static final Item LAUDANUM = new ItemLaudanum();
    public static final Item TRANQUILIZER = new ItemTranquilizer();
    //todo actually implement zombie curing property lul
    public static final Item RE_AGENT_SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP).recipeRemainder(SYRINGE).maxCount(1));

    public static final Item WAX = new Item(new Item.Settings().group(Constants.MM_GROUP));

    public static final Item PROTAGONIST_SPAWN_EGG = new SpawnEggItem(ModEntities.PROTAGONIST, 4137472, 14592, new Item.Settings().group(ItemGroup.MISC));

    public static void init() {
        Util.registerBlock(DUMMY_RESONATOR_OFF, "resonator_off");
        Util.registerBlock(DUMMY_RESONATOR_ON, "resonator_on");
        Util.registerBlock(POWERCELL, "powercell");

        Util.register(Registry.BLOCK_ENTITY_TYPE, "chemistry_set", CHEMISTRY_SET_BLOCK_ENTITY_TYPE);
        Util.registerBlock(CHEMISTRY_SET, "chemistry_set");

        Util.register(Registry.BLOCK_ENTITY_TYPE, "altar", ALTAR_BLOCK_ENTITY_TYPE);
        Util.registerBlock(BLACKSTONE_ALTAR, "blackstone_altar");
        Util.registerBlock(CORAL_ALTAR, "coral_altar");
        Util.registerBlock(FUNKY_ALTAR, "funky_altar");
        Util.registerBlock(MOSSY_STONE_ALTAR, "mossy_stone_altar");
        Util.registerBlock(NETHER_BRICK_ALTAR, "nether_brick_altar");
        Util.registerBlock(PRISMARINE_ALTAR, "prismarine_altar");
        Util.registerBlock(SANDSTONE_ALTAR, "sandstone_altar");
        Util.registerBlock(STONE_ALTAR, "stone_altar");

        Util.register(Registry.BLOCK_ENTITY_TYPE, "octagram", OCTAGRAM_BLOCK_ENTITY_TYPE);
        Util.registerBlock(CTHULHU_OCTAGRAM, "cthulhu_octagram");
        Util.registerBlock(HASTUR_OCTAGRAM, "hastur_octagram");
        Util.registerBlock(SHUB_OCTAGRAM, "shub_octagram");

        Util.register(Registry.BLOCK_ENTITY_TYPE, "statue", STATUE_BLOCK_ENTITY_TYPE);
        Util.registerBlock(CTHULHU_STATUE_GOLD, "cthulhu_statue_gold");
        Util.registerBlock(CTHULHU_STATUE_MOSSY, "cthulhu_statue_mossy");
        Util.registerBlock(CTHULHU_STATUE_PRISMARINE, "cthulhu_statue_prismarine");
        Util.registerBlock(CTHULHU_STATUE_STONE, "cthulhu_statue_stone");
        Util.registerBlock(HASTUR_STATUE_GOLD, "hastur_statue_gold");
        Util.registerBlock(HASTUR_STATUE_MOSSY, "hastur_statue_mossy");
        Util.registerBlock(HASTUR_STATUE_TERRACOTTA, "hastur_statue_terracotta");
        Util.registerBlock(HASTUR_STATUE_STONE, "hastur_statue_stone");
        Util.registerBlock(SHUB_STATUE_GOLD, "shub_statue_gold");
        Util.registerBlock(SHUB_STATUE_MOSSY, "shub_statue_mossy");
        Util.registerBlock(SHUB_STATUE_BLACKSTONE, "shub_statue_blackstone");
        Util.registerBlock(SHUB_STATUE_STONE, "shub_statue_stone");

        Util.registerBlock(STONE_CTHULHU_MURAL, "stone_cthulhu_mural");
        Util.registerBlock(MOSSY_CTHULHU_MURAL, "mossy_cthulhu_mural");
        Util.registerBlock(CORAL_CTHULHU_MURAL, "coral_cthulhu_mural");
        Util.registerBlock(PRISMARINE_CTHULHU_MURAL, "prismarine_cthulhu_mural");

        Util.registerBlock(STONE_HASTUR_MURAL, "stone_hastur_mural");
        Util.registerBlock(MOSSY_HASTUR_MURAL, "mossy_hastur_mural");
        Util.registerBlock(TERRACOTTA_HASTUR_MURAL, "terracotta_hastur_mural");
        Util.registerBlock(YELLOW_TERRACOTTA_HASTUR_MURAL, "yellow_terracotta_hastur_mural");

        Util.registerBlock(STONE_SHUB_MURAL, "stone_shub_mural");
        Util.registerBlock(MOSSY_SHUB_MURAL, "mossy_shub_mural");


        Util.registerBlock(OCEANIC_GOLD_BLOCK, "oceanic_gold_block");
        Util.registerBlock(WARDED_OCEANIC_GOLD_BLOCK, "oceanic_gold_block_warded");
        Util.registerBlock(OCEANIC_GOLD_TILES, "oceanic_gold_tiles");
        Util.registerBlock(OCEANIC_GOLD_PILLAR, "oceanic_gold_pillar");
        Util.registerBlock(OCEANIC_GOLD_PILLAR_ORNATE, "oceanic_gold_pillar_ornate");
        Util.registerBlock(OCEANIC_GOLD_PILLAR_SPLIT, "oceanic_gold_pillar_split");

        Util.registerBlock(CANDLE, "candle");

        Util.register(Registry.ITEM, "science_journal", SCIENCE_JOURNAL);
        Util.register(Registry.ITEM, "necronomicon", NECRONOMICON);

        Util.register(Registry.ITEM, "oceanic_gold", OCEANIC_GOLD);

        Util.register(Registry.ITEM, "rifle", RIFLE);
        Util.register(Registry.ITEM, "revolver", REVOLVER);
        Util.register(Registry.ITEM, "bullet", BULLET);

        Util.register(Registry.ITEM, "syringe", SYRINGE);
        Util.register(Registry.ITEM, "infested_wheat", INFESTED_WHEAT);
        Util.register(Registry.ITEM, "blotter", BLOTTER);
        Util.register(Registry.ITEM, "laudanum", LAUDANUM);
        Util.register(Registry.ITEM, "tranquilizer", TRANQUILIZER);
        Util.register(Registry.ITEM, "re_agent_syringe", RE_AGENT_SYRINGE);

        Util.register(Registry.ITEM, "wax", WAX);

        Util.register(Registry.ITEM, "protagonist_spawn_egg", PROTAGONIST_SPAWN_EGG);

    }
}

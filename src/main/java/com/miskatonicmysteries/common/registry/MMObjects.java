package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;
import com.miskatonicmysteries.api.block.AltarBlock;
import com.miskatonicmysteries.api.block.OctagramBlock;
import com.miskatonicmysteries.api.block.StatueBlock;
import com.miskatonicmysteries.api.item.BlessedSwordItem;
import com.miskatonicmysteries.api.item.ChalkItem;
import com.miskatonicmysteries.api.item.MMBookItem;
import com.miskatonicmysteries.api.item.trinkets.MaskTrinketItem;
import com.miskatonicmysteries.common.feature.block.*;
import com.miskatonicmysteries.common.feature.block.blockentity.AltarBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.ChemistrySetBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.MasterpieceStatueBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.StatueBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.energy.PowerCellBlockEntity;
import com.miskatonicmysteries.common.feature.block.blockentity.energy.ResonatorBlockEntity;
import com.miskatonicmysteries.common.feature.item.IncantationYogItem;
import com.miskatonicmysteries.common.feature.item.RevolverItem;
import com.miskatonicmysteries.common.feature.item.RifleItem;
import com.miskatonicmysteries.common.feature.item.WardingMarkItem;
import com.miskatonicmysteries.common.feature.item.YellowSignPatternItem;
import com.miskatonicmysteries.common.feature.item.armor.HasturCultistArmor;
import com.miskatonicmysteries.common.feature.item.armor.ShubCultistArmor;
import com.miskatonicmysteries.common.feature.item.consumable.BlotterItem;
import com.miskatonicmysteries.common.feature.item.consumable.ChemicalFuelItem;
import com.miskatonicmysteries.common.feature.item.consumable.CirrhosusFleshItem;
import com.miskatonicmysteries.common.feature.item.consumable.LaudanumItem;
import com.miskatonicmysteries.common.feature.item.consumable.ReAgentItem;
import com.miskatonicmysteries.common.feature.item.consumable.TheOrbItem;
import com.miskatonicmysteries.common.feature.item.consumable.TranquilizerItem;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

public class MMObjects {

	public static final Block CHEMISTRY_SET = new ChemistrySetBlock();
	public static final BlockEntityType<ChemistrySetBlockEntity> CHEMISTRY_SET_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(ChemistrySetBlockEntity::new, CHEMISTRY_SET).build(null);

	public static final Block BLACKSTONE_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.BLACKSTONE).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final Block CORAL_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.DEAD_BRAIN_CORAL_BLOCK).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final Block FUNKY_ALTAR = new AltarBlock(false, AbstractBlock.Settings.copy(Blocks.PURPUR_BLOCK).luminance(state -> 12));
	public static final Block MOSSY_STONE_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final Block NETHER_BRICK_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.NETHER_BRICKS).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final Block PRISMARINE_ALTAR = new AltarBlock(false,
		AbstractBlock.Settings.copy(Blocks.PRISMARINE).luminance(state -> 8));
	public static final Block SANDSTONE_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.SANDSTONE).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final Block STONE_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.STONE).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final Block DEEPSLATE_ALTAR = new AltarBlock(true,
		AbstractBlock.Settings.copy(Blocks.DEEPSLATE).luminance(state -> state.get(Properties.WATERLOGGED) ? 0 : 8));
	public static final BlockEntityType<AltarBlockEntity> ALTAR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(AltarBlockEntity::new, AltarBlock.ALTARS.toArray(new AltarBlock[AltarBlock.ALTARS.size()])).build(null);

	public static final Block OCTAGRAM_SIDES = new OctagramBlock.BlockOuterOctagram();
	public static final OctagramBlock CTHULHU_OCTAGRAM = new OctagramBlock(MMAffiliations.CTHULHU);
	public static final OctagramBlock HASTUR_OCTAGRAM = new OctagramBlock(MMAffiliations.HASTUR);
	public static final OctagramBlock SHUB_OCTAGRAM = new OctagramBlock(MMAffiliations.SHUB);
	public static final OctagramBlock YOG_OCTAGRAM = new OctagramBlock(MMAffiliations.YOG);

	public static final ChalkItem CTHULHU_CHALK = new ChalkItem(CTHULHU_OCTAGRAM,
		new Item.Settings().group(Constants.MM_GROUP).maxDamage(4));
	public static final ChalkItem HASTUR_CHALK = new ChalkItem(HASTUR_OCTAGRAM, new Item.Settings().group(Constants.MM_GROUP).maxDamage(4));
	public static final ChalkItem SHUB_CHALK = new ChalkItem(SHUB_OCTAGRAM, new Item.Settings().group(Constants.MM_GROUP).maxDamage(4));
	public static final ChalkItem YOG_CHALK = new ChalkItem(YOG_OCTAGRAM, new Item.Settings().group(Constants.MM_GROUP).maxDamage(4));
	public static final BlockEntityType<OctagramBlockEntity> OCTAGRAM_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(OctagramBlockEntity::new, OctagramBlock.OCTAGRAMS.toArray(new OctagramBlock[OctagramBlock.OCTAGRAMS.size()])).build(null);
	public static final StatueBlock CTHULHU_STATUE_GOLD = new StatueBlock(MMAffiliations.CTHULHU, true,
		AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
	public static final StatueBlock CTHULHU_STATUE_MOSSY = new StatueBlock(MMAffiliations.CTHULHU, false,
		AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final StatueBlock CTHULHU_STATUE_PRISMARINE = new StatueBlock(MMAffiliations.CTHULHU, false,
		AbstractBlock.Settings.copy(Blocks.PRISMARINE));
	public static final StatueBlock CTHULHU_STATUE_STONE = new StatueBlock(MMAffiliations.CTHULHU, false,
		AbstractBlock.Settings.copy(Blocks.STONE));

	public static final StatueBlock HASTUR_STATUE_GOLD = new StatueBlock(MMAffiliations.HASTUR, true,
		AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
	public static final StatueBlock HASTUR_STATUE_MOSSY = new StatueBlock(MMAffiliations.HASTUR, false,
		AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final StatueBlock HASTUR_STATUE_TERRACOTTA = new StatueBlock(MMAffiliations.HASTUR, false,
		AbstractBlock.Settings.copy(Blocks.TERRACOTTA));
	public static final StatueBlock HASTUR_STATUE_STONE = new StatueBlock(MMAffiliations.HASTUR, false,
		AbstractBlock.Settings.copy(Blocks.STONE));

	public static final StatueBlock SHUB_STATUE_GOLD = new StatueBlock(MMAffiliations.SHUB, true,
		AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
	public static final StatueBlock SHUB_STATUE_MOSSY = new StatueBlock(MMAffiliations.SHUB, false,
		AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final StatueBlock SHUB_STATUE_DEEPSLATE = new StatueBlock(MMAffiliations.SHUB, false,
		AbstractBlock.Settings.copy(Blocks.DEEPSLATE));
	public static final StatueBlock SHUB_STATUE_STONE = new StatueBlock(MMAffiliations.SHUB, false,
		AbstractBlock.Settings.copy(Blocks.STONE));

	public static final StatueBlock YOG_STATUE_GOLD = new StatueBlock(MMAffiliations.YOG, true,
	AbstractBlock.Settings.copy(Blocks.GOLD_BLOCK));
	public static final StatueBlock YOG_STATUE_MOSSY = new StatueBlock(MMAffiliations.YOG, false,
	AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final StatueBlock YOG_STATUE_END_STONE = new StatueBlock(MMAffiliations.YOG, false,
	AbstractBlock.Settings.copy(Blocks.END_STONE));
	public static final StatueBlock YOG_STATUE_STONE = new StatueBlock(MMAffiliations.YOG, false,
	AbstractBlock.Settings.copy(Blocks.STONE));
	public static final StatueBlock YOG_STATUE_BLACKSTONE = new StatueBlock(MMAffiliations.YOG, false,
	AbstractBlock.Settings.copy(Blocks.BLACKSTONE));



	public static final MasterpieceStatueBlock MASTERPIECE_STATUE = new MasterpieceStatueBlock(AbstractBlock.Settings.copy(Blocks.STONE));
	public static final BlockEntityType<MasterpieceStatueBlockEntity> MASTERPIECE_STATUE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(MasterpieceStatueBlockEntity::new, MASTERPIECE_STATUE).build(null);


	public static final Block STONE_CTHULHU_MURAL = new MuralBlock(MMAffiliations.CTHULHU, AbstractBlock.Settings.copy(Blocks.STONE));
	public static final Block MOSSY_CTHULHU_MURAL = new MuralBlock(MMAffiliations.CTHULHU,
		AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final Block CORAL_CTHULHU_MURAL = new MuralBlock(MMAffiliations.CTHULHU,
		AbstractBlock.Settings.copy(Blocks.DEAD_BRAIN_CORAL_BLOCK));
	public static final Block PRISMARINE_CTHULHU_MURAL = new MuralBlock(MMAffiliations.CTHULHU,
		AbstractBlock.Settings.copy(Blocks.PRISMARINE));


	public static final Block STONE_HASTUR_MURAL = new MuralBlock(MMAffiliations.HASTUR, AbstractBlock.Settings.copy(Blocks.STONE));
	public static final Block MOSSY_HASTUR_MURAL = new MuralBlock(MMAffiliations.HASTUR,
		AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final Block TERRACOTTA_HASTUR_MURAL = new MuralBlock(MMAffiliations.HASTUR,
		AbstractBlock.Settings.copy(Blocks.TERRACOTTA));
	public static final Block YELLOW_TERRACOTTA_HASTUR_MURAL = new MuralBlock(MMAffiliations.HASTUR,
		AbstractBlock.Settings.copy(Blocks.YELLOW_TERRACOTTA));

	public static final Block STONE_SHUB_MURAL = new MuralBlock(MMAffiliations.SHUB, AbstractBlock.Settings.copy(Blocks.STONE));
	public static final Block MOSSY_SHUB_MURAL = new MuralBlock(MMAffiliations.SHUB, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final Block DEEPSLATE_SHUB_MURAL = new MuralBlock(MMAffiliations.SHUB, AbstractBlock.Settings.copy(Blocks.BLACKSTONE));
	public static final Block BLACKSTONE_SHUB_MURAL = new MuralBlock(MMAffiliations.SHUB,
		AbstractBlock.Settings.copy(Blocks.GILDED_BLACKSTONE));

	public static final Block STONE_YOG_MURAL = new MuralBlock(MMAffiliations.YOG, AbstractBlock.Settings.copy(Blocks.STONE));
	public static final Block MOSSY_YOG_MURAL = new MuralBlock(MMAffiliations.YOG, AbstractBlock.Settings.copy(Blocks.MOSSY_COBBLESTONE));
	public static final Block BLACKSTONE_YOG_MURAL = new MuralBlock(MMAffiliations.YOG, AbstractBlock.Settings.copy(Blocks.BLACKSTONE));
	public static final Block END_STONE_YOG_MURAL = new MuralBlock(MMAffiliations.YOG,
		AbstractBlock.Settings.copy(Blocks.END_STONE));

	public static final Block OCEANIC_GOLD_BLOCK = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
	public static final Block WARDED_OCEANIC_GOLD_BLOCK = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD.strength(2.5F, 12));
	public static final Block OCEANIC_GOLD_TILES = new Block(Constants.BlockSettings.OCEANIC_GOLD);

	public static final Block OCEANIC_GOLD_PILLAR = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
	public static final Block OCEANIC_GOLD_PILLAR_ORNATE = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);
	public static final Block OCEANIC_GOLD_PILLAR_SPLIT = new PillarBlock(Constants.BlockSettings.OCEANIC_GOLD);


	public static final Block ELDERIAN_STONE = new ElderianBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_STONE_STAIRS = new ElderianStairsBlock(ELDERIAN_STONE,Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_STONE_SLAB = new ElderianSlabBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_STONE_WALL = new WallBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_STONE_SMOOTH = new Block(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_STONE_SMOOTH_STAIRS = new ElderianStairsBlock(ELDERIAN_STONE,Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_STONE_SMOOTH_SLAB = new ElderianSlabBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_STONE_SMOOTH_WALL = new WallBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_STONE_CHISELED = new Block(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_BRICKS = new ElderianBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_BRICKS_STAIRS = new ElderianStairsBlock(ELDERIAN_STONE,Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_BRICKS_SLAB = new ElderianSlabBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_BRICKS_WALL = new WallBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_CRACKED_BRICKS = new ElderianBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_CRACKED_STAIRS = new ElderianStairsBlock(ELDERIAN_STONE,Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_CRACKED_SLAB = new ElderianSlabBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_CRACKED_WALL = new WallBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_ICED_BRICKS = new ElderianBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_MOSSY_BRICKS = new ElderianBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_MOSSY_STAIRS = new ElderianStairsBlock(ELDERIAN_STONE,Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_MOSSY_SLAB = new ElderianSlabBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_MOSSY_WALL = new WallBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_PILLAR_SMOOTH = new ElderianPillarBlock(Constants.BlockSettings.ELDERIAN);
	public static final Block ELDERIAN_PILLAR_TENTACLED = new ElderianPillarBlock(Constants.BlockSettings.ELDERIAN);

	public static final Block ELDERIAN_CTHULHU_MURAL = new MuralBlock(MMAffiliations.CTHULHU,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));
	public static final Block ELDERIAN_HASTUR_MURAL = new MuralBlock(MMAffiliations.HASTUR,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));
	public static final Block ELDERIAN_SHUB_MURAL = new MuralBlock(MMAffiliations.SHUB,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));
	public static final Block ELDERIAN_YOG_MURAL = new MuralBlock(MMAffiliations.YOG,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));

	public static final StatueBlock CTHULHU_STATUE_ELDERIAN = new StatueBlock(MMAffiliations.CTHULHU, false,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));
	public static final StatueBlock HASTUR_STATUE_ELDERIAN = new StatueBlock(MMAffiliations.HASTUR, false,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));
	public static final StatueBlock SHUB_STATUE_ELDERIAN = new StatueBlock(MMAffiliations.SHUB, false,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));
	public static final StatueBlock YOG_STATUE_ELDERIAN = new StatueBlock(MMAffiliations.YOG, false,
	AbstractBlock.Settings.copy(ELDERIAN_STONE));


	public static final BlockEntityType<StatueBlockEntity> STATUE_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
	.create(StatueBlockEntity::new, StatueBlock.STATUES.toArray(new StatueBlock[StatueBlock.STATUES.size()])).build(null);

	public static final Block RESONATOR = new ResonatorBlock();
	public static final BlockEntityType<ResonatorBlockEntity> RESONATOR_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(ResonatorBlockEntity::new, RESONATOR).build(null);

	public static final Block POWER_CELL = new PowerCellBlock();
	public static final BlockEntityType<PowerCellBlockEntity> POWER_CELL_BLOCK_ENTITY_TYPE = FabricBlockEntityTypeBuilder
		.create(PowerCellBlockEntity::new, POWER_CELL).build(null);

	public static final Block YELLOW_SIGN = new YellowSignBlock();
	public static final Block WARDING_MARK = new WardingMarkBlock();

	public static final Block INFESTED_WHEAT_CROP = new InfestedWheatCropBlock();
	public static final Block BIRCH_LOG = new PillarBlock(AbstractBlock.Settings
		.of(Material.WOOD, (blockState) -> blockState.get(PillarBlock.AXIS) == Direction.Axis.Y ? MapColor.PALE_YELLOW : MapColor.WHITE)
		.strength(2.0F).sounds(BlockSoundGroup.WOOD));

	public static final LoomPattern YELLOW_SIGN_BANNER = new LoomPattern(true);
	public static final Item YELLOW_SIGN_LOOM_PATTERN = new YellowSignPatternItem();



	public static final MMBookItem SCIENCE_JOURNAL = new MMBookItem(new Identifier(Constants.MOD_ID, "science_journal"),
		MMAffiliations.NONE, false, new Item.Settings().maxCount(1).group(Constants.MM_GROUP));
	public static final MMBookItem NECRONOMICON = new MMBookItem(new Identifier(Constants.MOD_ID, "necronomicon"), MMAffiliations.NONE,
		true, new Item.Settings().maxCount(1).group(Constants.MM_GROUP));

	public static final Item OCEANIC_GOLD = new Item(new Item.Settings().group(Constants.MM_GROUP));

	public static final Item RIFLE = new RifleItem();
	public static final Item REVOLVER = new RevolverItem();
	public static final Item BULLET = new Item(new Item.Settings().group(Constants.MM_GROUP));

	public static final Item SYRINGE = new Item(new Item.Settings().group(Constants.MM_GROUP));

	public static final Item INFESTED_WHEAT = new Item(new Item.Settings().group(Constants.MM_GROUP));

	public static final Item BLOTTER = new BlotterItem();
	public static final Item LAUDANUM = new LaudanumItem();
	public static final Item TRANQUILIZER = new TranquilizerItem();
	public static final Item RE_AGENT_SYRINGE = new ReAgentItem();

	public static final Item THE_ORB = new TheOrbItem();

	public static final Item ORNATE_DAGGER = new BlessedSwordItem(MMAffiliations.HASTUR, 2, -2.3F,
		(target, attacker) -> target.addStatusEffect(new StatusEffectInstance(MMStatusEffects.MANIA, 100, 1, true, false, false)),
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item GUTTING_DAGGER = new BlessedSwordItem(MMAffiliations.SHUB, 3, -2.6F,
		(target, attacker) -> target.addStatusEffect(new StatusEffectInstance(MMStatusEffects.BLEED, 600, 0, false, false, false)),
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item HORNED_DAGGER = new BlessedSwordItem(MMAffiliations.SHUB, 4, -2.3F, (target, attacker) -> {
	},
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item FISHY_DAGGER = new BlessedSwordItem(MMAffiliations.CTHULHU, 3, -2.3F,
		(target, attacker) -> target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0, false, true, true)),
		new Item.Settings().group(Constants.MM_GROUP));

	public static final Item STELLAR_DAGGER = new BlessedSwordItem(MMAffiliations.YOG, 3, -2.3F,
	(target, attacker) -> target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0, false, true, true)),
	new Item.Settings().group(Constants.MM_GROUP));//TODO change name from YOG_DAGGER

	public static final Item PROTAGONIST_SPAWN_EGG = new SpawnEggItem(MMEntities.PROTAGONIST, 4137472, 14592,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item HASTUR_CULTIST_SPAWN_EGG = new SpawnEggItem(MMEntities.HASTUR_CULTIST, 0xEAC800, 0xFFFFFF,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item PHANTASMA_SPAWN_EGG = new SpawnEggItem(MMEntities.PHANTASMA, 0x77329F, 0xDA329F,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item ABERRATION_SPAWN_EGG = new SpawnEggItem(MMEntities.ABERRATION, 0x77329F, 0x8c2066,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item BYAKHEE_SPAWN_EGG = new SpawnEggItem(MMEntities.BYAKHEE, 0x5a6049, 0xEAC800,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item TATTERED_PRINCE_SPAWN_EGG = new SpawnEggItem(MMEntities.TATTERED_PRINCE, 0xEAC800, 0xFF0000,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item TENTACLE_SPAWN_EGG = new SpawnEggItem(MMEntities.GENERIC_TENTACLE, 0xf9da5c, 0xefc623,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item HARROW_SPAWN_EGG = new SpawnEggItem(MMEntities.HARROW, 0xefc623, 0xf9da5c,
		new Item.Settings().group(Constants.MM_GROUP));
	public static final Item TINDALOS_HOUND_SPAWN_EGG = new SpawnEggItem(MMEntities.TINDALOS_HOUND, 0x0b5c8c, 0x284b7f,
		new Item.Settings().group(Constants.MM_GROUP));

	public static final Item ELEGANT_MASK = new MaskTrinketItem(MMAffiliations.HASTUR, false,
		new Item.Settings().group(Constants.MM_GROUP).maxCount(1));
	public static final Item FERAL_MASK = new MaskTrinketItem(MMAffiliations.SHUB, false,
		new Item.Settings().group(Constants.MM_GROUP).maxCount(1));
	public static final Item WILD_MASK = new MaskTrinketItem(MMAffiliations.SHUB, false,
		new Item.Settings().group(Constants.MM_GROUP).maxCount(1));

	public static final Item YELLOW_HOOD = new HasturCultistArmor(EquipmentSlot.HEAD);
	public static final Item YELLOW_ROBE = new HasturCultistArmor(EquipmentSlot.CHEST);
	public static final Item YELLOW_SKIRT = new HasturCultistArmor(EquipmentSlot.LEGS);

	public static final Item DARK_HOOD = new ShubCultistArmor(EquipmentSlot.HEAD);
	public static final Item DARK_ROBE = new ShubCultistArmor(EquipmentSlot.CHEST);
	public static final Item DARK_SKIRT = new ShubCultistArmor(EquipmentSlot.LEGS);

	public static final Item INCANTATION_YOG = new IncantationYogItem();

	public static final Item IRIDESCENT_PEARL = new Item(new Item.Settings().group(Constants.MM_GROUP));
	public static final Item RESONATE_OOZE = new Item(new Item.Settings().group(Constants.MM_GROUP));

	public static final Item CHEMICAL_FUEL = new ChemicalFuelItem();

	public static final Item CIRRHOSUS_FLESH = new CirrhosusFleshItem();
	public static final Item WARDED_PAPER = new WardingMarkItem();

	public static void init() {
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(MMObjects.INFESTED_WHEAT, 0.7F);

		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "chemistry_set", CHEMISTRY_SET_BLOCK_ENTITY_TYPE);
		RegistryUtil.registerBlock(CHEMISTRY_SET, "chemistry_set");

		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "altar", ALTAR_BLOCK_ENTITY_TYPE);
		RegistryUtil.registerBlock(BLACKSTONE_ALTAR, "blackstone_altar");
		RegistryUtil.registerBlock(CORAL_ALTAR, "coral_altar");
		RegistryUtil.registerBlock(FUNKY_ALTAR, "funky_altar");
		RegistryUtil.registerBlock(MOSSY_STONE_ALTAR, "mossy_stone_altar");
		RegistryUtil.registerBlock(NETHER_BRICK_ALTAR, "nether_brick_altar");
		RegistryUtil.registerBlock(PRISMARINE_ALTAR, "prismarine_altar");
		RegistryUtil.registerBlock(SANDSTONE_ALTAR, "sandstone_altar");
		RegistryUtil.registerBlock(STONE_ALTAR, "stone_altar");
		RegistryUtil.registerBlock(DEEPSLATE_ALTAR, "deepslate_altar");

		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "octagram", OCTAGRAM_BLOCK_ENTITY_TYPE);
		RegistryUtil.register(Registry.BLOCK, "octagram_sides", OCTAGRAM_SIDES);

		RegistryUtil.register(Registry.BLOCK, "cthulhu_octagram", CTHULHU_OCTAGRAM);
		RegistryUtil.register(Registry.BLOCK, "hastur_octagram", HASTUR_OCTAGRAM);
		RegistryUtil.register(Registry.BLOCK, "shub_octagram", SHUB_OCTAGRAM);
		RegistryUtil.register(Registry.BLOCK, "yog_octagram", YOG_OCTAGRAM);
		RegistryUtil.register(Registry.ITEM, "cthulhu_chalk", CTHULHU_CHALK);
		RegistryUtil.register(Registry.ITEM, "hastur_chalk", HASTUR_CHALK);
		RegistryUtil.register(Registry.ITEM, "shub_chalk", SHUB_CHALK);
		RegistryUtil.register(Registry.ITEM, "yog_chalk", YOG_CHALK);
		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "statue", STATUE_BLOCK_ENTITY_TYPE);
		RegistryUtil.registerBlock(CTHULHU_STATUE_GOLD, "cthulhu_statue_gold");
		RegistryUtil.registerBlock(CTHULHU_STATUE_MOSSY, "cthulhu_statue_mossy");
		RegistryUtil.registerBlock(CTHULHU_STATUE_PRISMARINE, "cthulhu_statue_prismarine");
		RegistryUtil.registerBlock(CTHULHU_STATUE_STONE, "cthulhu_statue_stone");
		RegistryUtil.registerBlock(CTHULHU_STATUE_ELDERIAN, "cthulhu_statue_elderian");
		RegistryUtil.registerBlock(HASTUR_STATUE_GOLD, "hastur_statue_gold");
		RegistryUtil.registerBlock(HASTUR_STATUE_MOSSY, "hastur_statue_mossy");
		RegistryUtil.registerBlock(HASTUR_STATUE_TERRACOTTA, "hastur_statue_terracotta");
		RegistryUtil.registerBlock(HASTUR_STATUE_STONE, "hastur_statue_stone");
		RegistryUtil.registerBlock(HASTUR_STATUE_ELDERIAN, "hastur_statue_elderian");
		RegistryUtil.registerBlock(SHUB_STATUE_GOLD, "shub_statue_gold");
		RegistryUtil.registerBlock(SHUB_STATUE_MOSSY, "shub_statue_mossy");
		RegistryUtil.registerBlock(SHUB_STATUE_DEEPSLATE, "shub_statue_deepslate");
		RegistryUtil.registerBlock(SHUB_STATUE_STONE, "shub_statue_stone");
		RegistryUtil.registerBlock(SHUB_STATUE_ELDERIAN, "shub_statue_elderian");
		RegistryUtil.registerBlock(YOG_STATUE_GOLD, "yog_statue_gold");
		RegistryUtil.registerBlock(YOG_STATUE_MOSSY, "yog_statue_mossy");
		RegistryUtil.registerBlock(YOG_STATUE_BLACKSTONE, "yog_statue_blackstone");
		RegistryUtil.registerBlock(YOG_STATUE_END_STONE, "yog_statue_end_stone");
		RegistryUtil.registerBlock(YOG_STATUE_STONE, "yog_statue_stone");
		RegistryUtil.registerBlock(YOG_STATUE_ELDERIAN, "yog_statue_elderian");

		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "masterpiece_statue", MASTERPIECE_STATUE_BLOCK_ENTITY_TYPE);
		RegistryUtil.register(Registry.BLOCK, "masterpiece_statue", MASTERPIECE_STATUE);
		RegistryUtil.register(Registry.ITEM, "masterpiece_statue", new MasterpieceStatueBlock.MasterpieceStatueBlockItem());

		RegistryUtil.registerBlock(STONE_CTHULHU_MURAL, "stone_cthulhu_mural");
		RegistryUtil.registerBlock(MOSSY_CTHULHU_MURAL, "mossy_cthulhu_mural");
		RegistryUtil.registerBlock(CORAL_CTHULHU_MURAL, "coral_cthulhu_mural");
		RegistryUtil.registerBlock(PRISMARINE_CTHULHU_MURAL, "prismarine_cthulhu_mural");
		RegistryUtil.registerBlock(ELDERIAN_CTHULHU_MURAL, "elderian_cthulhu_mural");

		RegistryUtil.registerBlock(STONE_HASTUR_MURAL, "stone_hastur_mural");
		RegistryUtil.registerBlock(MOSSY_HASTUR_MURAL, "mossy_hastur_mural");
		RegistryUtil.registerBlock(TERRACOTTA_HASTUR_MURAL, "terracotta_hastur_mural");
		RegistryUtil.registerBlock(YELLOW_TERRACOTTA_HASTUR_MURAL, "yellow_terracotta_hastur_mural");
		RegistryUtil.registerBlock(ELDERIAN_HASTUR_MURAL, "elderian_hastur_mural");

		RegistryUtil.registerBlock(STONE_SHUB_MURAL, "stone_shub_mural");
		RegistryUtil.registerBlock(MOSSY_SHUB_MURAL, "mossy_shub_mural");
		RegistryUtil.registerBlock(DEEPSLATE_SHUB_MURAL, "deepslate_shub_mural");
		RegistryUtil.registerBlock(BLACKSTONE_SHUB_MURAL, "blackstone_shub_mural");
		RegistryUtil.registerBlock(ELDERIAN_SHUB_MURAL, "elderian_shub_mural");

		RegistryUtil.registerBlock(STONE_YOG_MURAL, "stone_yog_mural");
		RegistryUtil.registerBlock(MOSSY_YOG_MURAL, "mossy_yog_mural");
		RegistryUtil.registerBlock(BLACKSTONE_YOG_MURAL, "blackstone_yog_mural");
		RegistryUtil.registerBlock(END_STONE_YOG_MURAL, "end_stone_yog_mural");
		RegistryUtil.registerBlock(ELDERIAN_YOG_MURAL, "elderian_yog_mural");

		RegistryUtil.registerBlock(OCEANIC_GOLD_BLOCK, "oceanic_gold_block");
		RegistryUtil.registerBlock(WARDED_OCEANIC_GOLD_BLOCK, "oceanic_gold_block_warded");
		RegistryUtil.registerBlock(OCEANIC_GOLD_TILES, "oceanic_gold_tiles");
		RegistryUtil.registerBlock(OCEANIC_GOLD_PILLAR, "oceanic_gold_pillar");
		RegistryUtil.registerBlock(OCEANIC_GOLD_PILLAR_ORNATE, "oceanic_gold_pillar_ornate");
		RegistryUtil.registerBlock(OCEANIC_GOLD_PILLAR_SPLIT, "oceanic_gold_pillar_split");

		RegistryUtil.registerBlock(ELDERIAN_BRICKS, "elderian_bricks");
		RegistryUtil.registerBlock(ELDERIAN_BRICKS_STAIRS, "elderian_bricks_stairs");
		RegistryUtil.registerBlock(ELDERIAN_BRICKS_SLAB, "elderian_bricks_slab");
		RegistryUtil.registerBlock(ELDERIAN_BRICKS_WALL, "elderian_bricks_wall");

		RegistryUtil.registerBlock(ELDERIAN_CRACKED_BRICKS, "elderian_bricks_cracked");
		RegistryUtil.registerBlock(ELDERIAN_CRACKED_STAIRS, "elderian_bricks_cracked_stairs");
		RegistryUtil.registerBlock(ELDERIAN_CRACKED_SLAB, "elderian_bricks_cracked_slab");
		RegistryUtil.registerBlock(ELDERIAN_CRACKED_WALL, "elderian_bricks_cracked_wall");

		RegistryUtil.registerBlock(ELDERIAN_ICED_BRICKS, "elderian_bricks_iced");

		RegistryUtil.registerBlock(ELDERIAN_MOSSY_BRICKS, "elderian_bricks_mossy");
		RegistryUtil.registerBlock(ELDERIAN_MOSSY_STAIRS, "elderian_bricks_mossy_stairs");
		RegistryUtil.registerBlock(ELDERIAN_MOSSY_SLAB, "elderian_bricks_mossy_slab");
		RegistryUtil.registerBlock(ELDERIAN_MOSSY_WALL, "elderian_bricks_mossy_wall");

		RegistryUtil.registerBlock(ELDERIAN_PILLAR_SMOOTH, "elderian_smooth_pillar");
		RegistryUtil.registerBlock(ELDERIAN_PILLAR_TENTACLED, "elderian_tentacled_pillar");


		RegistryUtil.registerBlock(ELDERIAN_STONE, "elderian_stone");
		RegistryUtil.registerBlock(ELDERIAN_STONE_STAIRS, "elderian_stone_stairs");
		RegistryUtil.registerBlock(ELDERIAN_STONE_SLAB, "elderian_stone_slab");
		RegistryUtil.registerBlock(ELDERIAN_STONE_WALL, "elderian_stone_wall");

		RegistryUtil.registerBlock(ELDERIAN_STONE_CHISELED, "elderian_stone_chiseled");

		RegistryUtil.registerBlock(ELDERIAN_STONE_SMOOTH, "elderian_stone_smooth");
		RegistryUtil.registerBlock(ELDERIAN_STONE_SMOOTH_STAIRS, "elderian_stone_smooth_stairs");
		RegistryUtil.registerBlock(ELDERIAN_STONE_SMOOTH_SLAB, "elderian_stone_smooth_slab");
		RegistryUtil.registerBlock(ELDERIAN_STONE_SMOOTH_WALL, "elderian_stone_smooth_wall");

		RegistryUtil.register(Registry.BLOCK, "yellow_sign", YELLOW_SIGN);
		RegistryUtil.register(Registry.BLOCK, "warding_mark", WARDING_MARK);
		RegistryUtil.register(Registry.BLOCK, "birch_log", BIRCH_LOG);
		RegistryUtil.register(Registry.BLOCK, "infested_wheat", INFESTED_WHEAT_CROP);

		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "resonator", RESONATOR_BLOCK_ENTITY_TYPE);
		RegistryUtil.registerBlock(RESONATOR, "resonator");
		RegistryUtil.register(Registry.BLOCK_ENTITY_TYPE, "power_cell", POWER_CELL_BLOCK_ENTITY_TYPE);
		RegistryUtil.registerBlock(POWER_CELL, "power_cell");

		RegistryUtil.register(LoomPatterns.REGISTRY, "yellow_sign", YELLOW_SIGN_BANNER);

		RegistryUtil.register(Registry.ITEM, "yellow_sign_banner_pattern", YELLOW_SIGN_LOOM_PATTERN);



		RegistryUtil.register(Registry.ITEM, "science_journal", SCIENCE_JOURNAL);
		RegistryUtil.register(Registry.ITEM, "necronomicon", NECRONOMICON);

		RegistryUtil.register(Registry.ITEM, "oceanic_gold", OCEANIC_GOLD);

		RegistryUtil.register(Registry.ITEM, "rifle", RIFLE);
		RegistryUtil.register(Registry.ITEM, "revolver", REVOLVER);
		RegistryUtil.register(Registry.ITEM, "bullet", BULLET);

		RegistryUtil.register(Registry.ITEM, "syringe", SYRINGE);
		RegistryUtil.register(Registry.ITEM, "infested_wheat", INFESTED_WHEAT);
		RegistryUtil.register(Registry.ITEM, "blotter", BLOTTER);
		RegistryUtil.register(Registry.ITEM, "laudanum", LAUDANUM);
		RegistryUtil.register(Registry.ITEM, "tranquilizer", TRANQUILIZER);
		RegistryUtil.register(Registry.ITEM, "re_agent_syringe", RE_AGENT_SYRINGE);

		RegistryUtil.register(Registry.ITEM, "ornate_dagger", ORNATE_DAGGER);
		RegistryUtil.register(Registry.ITEM, "gutting_dagger", GUTTING_DAGGER);
		RegistryUtil.register(Registry.ITEM, "horned_dagger", HORNED_DAGGER);
		RegistryUtil.register(Registry.ITEM, "fishy_dagger", FISHY_DAGGER);
		RegistryUtil.register(Registry.ITEM, "stellar_dagger", STELLAR_DAGGER);

		RegistryUtil.register(Registry.ITEM, "protagonist_spawn_egg", PROTAGONIST_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "hastur_cultist_spawn_egg", HASTUR_CULTIST_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "phantasma_spawn_egg", PHANTASMA_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "aberration_spawn_egg", ABERRATION_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "byakhee_spawn_egg", BYAKHEE_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "tattered_prince_spawn_egg", TATTERED_PRINCE_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "tentacle_spawn_egg", TENTACLE_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "harrow_spawn_egg", HARROW_SPAWN_EGG);
		RegistryUtil.register(Registry.ITEM, "tindalos_hound_spawn_egg", TINDALOS_HOUND_SPAWN_EGG);

		RegistryUtil.register(Registry.ITEM, "elegant_mask", ELEGANT_MASK);
		RegistryUtil.register(Registry.ITEM, "feral_mask", FERAL_MASK);
		RegistryUtil.register(Registry.ITEM, "wild_mask", WILD_MASK);

		RegistryUtil.register(Registry.ITEM, "yellow_hood", YELLOW_HOOD);
		RegistryUtil.register(Registry.ITEM, "yellow_robe", YELLOW_ROBE);
		RegistryUtil.register(Registry.ITEM, "yellow_skirt", YELLOW_SKIRT);

		RegistryUtil.register(Registry.ITEM, "dark_hood", DARK_HOOD);
		RegistryUtil.register(Registry.ITEM, "dark_robe", DARK_ROBE);
		RegistryUtil.register(Registry.ITEM, "dark_skirt", DARK_SKIRT);

		RegistryUtil.register(Registry.ITEM, "incantation_yog", INCANTATION_YOG);

		RegistryUtil.register(Registry.ITEM, "the_orb", THE_ORB);
		RegistryUtil.register(Registry.ITEM, "iridescent_pearl", IRIDESCENT_PEARL);
		RegistryUtil.register(Registry.ITEM, "resonate_ooze", RESONATE_OOZE);
		RegistryUtil.register(Registry.ITEM, "chemical_fuel", CHEMICAL_FUEL);

		RegistryUtil.register(Registry.ITEM, "cirrhosus_flesh", CIRRHOSUS_FLESH);
		RegistryUtil.register(Registry.ITEM, "warded_paper", WARDED_PAPER);
	}
}

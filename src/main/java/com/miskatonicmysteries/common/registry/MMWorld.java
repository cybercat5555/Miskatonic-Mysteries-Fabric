package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.common.MMMidnightLibConfig;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.feature.world.biome.HasturBiomeEffect;
import com.miskatonicmysteries.common.feature.world.processor.PsychonautHouseProcessor;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.RegistryUtil;

import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.BlockStateMatchRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.SpawnSettings.Builder;
import net.minecraft.world.biome.SpawnSettings.SpawnEntry;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

public class MMWorld {

	public static final StructureProcessorType<PsychonautHouseProcessor> PSYCHONAUT_PROCESSOR =
		StructureProcessorType.register(Constants.MOD_ID + ":psychonaut_house", PsychonautHouseProcessor.CODEC);
	public static final RegistryEntry<StructureProcessorList> ZOMBIE_PROCESSOR =
		registerProcessorList("psychonaut_zombie", new PsychonautHouseProcessor(10091940),
							  new RuleStructureProcessor(
								  ImmutableList.of(new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.8F),
																			  AlwaysTrueRuleTest.INSTANCE,
																			  Blocks.MOSSY_COBBLESTONE.getDefaultState()),
												   new StructureProcessorRule(new TagMatchRuleTest(BlockTags.DOORS), AlwaysTrueRuleTest.INSTANCE,
																			  Blocks.AIR.getDefaultState()),
												   new StructureProcessorRule(new BlockMatchRuleTest(Blocks.TORCH), AlwaysTrueRuleTest.INSTANCE,
																			  Blocks.AIR.getDefaultState()),
												   new StructureProcessorRule(new BlockMatchRuleTest(Blocks.WALL_TORCH), AlwaysTrueRuleTest.INSTANCE,
																			  Blocks.AIR.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.07F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.MOSSY_COBBLESTONE, 0.07F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHITE_TERRACOTTA, 0.07F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_LOG, 0.05F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_PLANKS, 0.1F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_STAIRS, 0.1F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.STRIPPED_OAK_LOG, 0.02F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GLASS_PANE, 0.5F),
																			  AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()),
												   new StructureProcessorRule(new BlockStateMatchRuleTest(Blocks.GLASS_PANE.getDefaultState()
																											  .with(PaneBlock.NORTH, true)
																											  .with(PaneBlock.SOUTH, true)),
																			  AlwaysTrueRuleTest.INSTANCE,
																			  Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState()
																				  .with(PaneBlock.NORTH, true)
																				  .with(PaneBlock.SOUTH, true)),
												   new StructureProcessorRule(new BlockStateMatchRuleTest(Blocks.GLASS_PANE.getDefaultState()
																											  .with(PaneBlock.EAST, true)
																											  .with(PaneBlock.WEST, true)),
																			  AlwaysTrueRuleTest.INSTANCE,
																			  Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState()
																				  .with(PaneBlock.EAST, true)
																				  .with(PaneBlock.WEST, true)))));
	public static final RegistryEntry<StructureProcessorList> NORMAL_PROCESSOR =
		registerProcessorList("psychonaut_normal", new PsychonautHouseProcessor(12081980),
							  new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(
								  new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.1F), AlwaysTrueRuleTest.INSTANCE,
								  Blocks.MOSSY_COBBLESTONE.getDefaultState()))));

	public static final Biome HASTUR_BIOME = creatHasturBiome();
	public static final BiomeEffect HASTUR_BIOME_EFFECT = new HasturBiomeEffect();

	private static Biome creatHasturBiome() {
		Biome.Builder biomeBuilder = new Biome.Builder();
		biomeBuilder.temperature(0.75F).precipitation(Biome.Precipitation.RAIN).downfall(0.3F)
			.generationSettings(new GenerationSettings.Builder()
									.build())
			.effects(new BiomeEffects.Builder()
						 .fogColor(0xEFC91F).skyColor(0x000000)
						 .waterColor(0x1199C6).waterFogColor(0x1199C6)
						 .grassColor(0xF2C709).foliageColor(0xE58E03)
						 .build());
		SpawnSettings.Builder spawnsBuilder = new Builder();
		spawnsBuilder.spawn(SpawnGroup.MONSTER, new SpawnEntry(MMEntities.GENERIC_TENTACLE, 40, 1, 4));
		spawnsBuilder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SKELETON, 80, 4, 4));
		spawnsBuilder.spawn(SpawnGroup.MONSTER, new SpawnEntry(MMEntities.HARROW, 20, 1, 3));
		spawnsBuilder.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.WITCH, 10, 1, 1));
		spawnsBuilder.spawn(SpawnGroup.MONSTER, new SpawnEntry(EntityType.ENDERMAN, 20, 1, 4));
		spawnsBuilder.spawn(SpawnGroup.MONSTER, new SpawnEntry(MMEntities.FEASTER, 1, 1, 1));
		spawnsBuilder.spawnCost(MMEntities.GENERIC_TENTACLE, 0.7F, 0.15F);
		spawnsBuilder.spawnCost(MMEntities.HARROW, 2, 0.1F);
		DefaultBiomeFeatures.addFarmAnimals(spawnsBuilder);
		biomeBuilder.spawnSettings(spawnsBuilder.build());
		spawnsBuilder.creatureSpawnProbability(0.025F);
		return biomeBuilder.build();
	}

	public static void init() {
		StructurePools.register(new StructurePool(new Identifier(Constants.MOD_ID, "village/common/hastur_cultist"),
												  new Identifier("empty"),
												  ImmutableList.of(Pair.of(StructurePoolElement.ofLegacySingle(Constants.MOD_ID + ":village/common" +
																												   "/hastur_cultist"), 1)),
												  StructurePool.Projection.RIGID));
		StructurePools.register(new StructurePool(new Identifier(Constants.MOD_ID, "village/common" +
			"/hastur_cultist_ascended"), new Identifier("empty"),
												  ImmutableList.of(Pair.of(StructurePoolElement.ofLegacySingle(Constants.MOD_ID + ":village/common" +
																												   "/hastur_cultist_ascended"), 1)),
												  StructurePool.Projection.RIGID));

		RegistryUtil.register(BuiltinRegistries.BIOME, "hastur", HASTUR_BIOME);
		BuiltinRegistries.BIOME.getKey(HASTUR_BIOME)
			.ifPresent(key -> MiskatonicMysteriesAPI.associateBiomeEffect(key, HASTUR_BIOME_EFFECT));

	}

	public static void specialInject(StructurePool pool) {
		RegistryUtil.tryAddElementToPool(new Identifier("village/plains/houses"), pool, Constants.MOD_ID +
											 ":village/plains/houses/plains_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/savanna/houses"), pool, Constants.MOD_ID +
											 ":village/savanna/houses/savanna_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/snowy/houses"), pool, Constants.MOD_ID +
											 ":village/snowy/houses/snowy_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/desert/houses"), pool, Constants.MOD_ID +
											 ":village/desert/houses/desert_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/taiga/houses"), pool, Constants.MOD_ID +
											 ":village/taiga/houses/taiga_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.NORMAL_PROCESSOR);

		RegistryUtil.tryAddElementToPool(new Identifier("village/plains/zombie/houses"), pool,
										 Constants.MOD_ID + ":village/plains/zombie/houses/plains_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.ZOMBIE_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/savanna/zombie/houses"), pool,
										 Constants.MOD_ID + ":village/savanna/zombie/houses/savanna_psychonaut", StructurePool.Projection.RIGID
			, MMMidnightLibConfig.psychonautHouseWeight, MMWorld.ZOMBIE_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/snowy/zombie/houses"), pool,
										 Constants.MOD_ID + ":village/snowy/zombie/houses/snowy_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.ZOMBIE_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/desert/zombie/houses"), pool,
										 Constants.MOD_ID + ":village/desert/zombie/houses/desert_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.ZOMBIE_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/taiga/zombie/houses"), pool,
										 Constants.MOD_ID + ":village/taiga/zombie/houses/taiga_psychonaut", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.psychonautHouseWeight, MMWorld.ZOMBIE_PROCESSOR);

		RegistryUtil.tryAddElementToPool(new Identifier("village/plains/town_centers"), pool,
										 Constants.MOD_ID + ":village/plains/town_centers/plains_shrine", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.hasturShrineWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/savanna/town_centers"), pool,
										 Constants.MOD_ID + ":village/savanna/town_centers/savanna_shrine", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.hasturShrineWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/snowy/town_centers"), pool,
										 Constants.MOD_ID + ":village/snowy/town_centers/snowy_shrine", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.hasturShrineWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/taiga/town_centers"), pool,
										 Constants.MOD_ID + ":village/taiga/town_centers/taiga_shrine", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.hasturShrineWeight, MMWorld.NORMAL_PROCESSOR);
		RegistryUtil.tryAddElementToPool(new Identifier("village/desert/town_centers"), pool,
										 Constants.MOD_ID + ":village/desert/town_centers/desert_shrine", StructurePool.Projection.RIGID,
				MMMidnightLibConfig.hasturShrineWeight, MMWorld.NORMAL_PROCESSOR);
	}

	private static RegistryEntry<StructureProcessorList> registerProcessorList(String id, StructureProcessor... processorList) {
		Identifier identifier = new Identifier(Constants.MOD_ID, id);
		StructureProcessorList structureProcessorList = new StructureProcessorList(List.of(processorList));
		return BuiltinRegistries.add(BuiltinRegistries.STRUCTURE_PROCESSOR_LIST, identifier, structureProcessorList);
	}
}

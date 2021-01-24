package com.miskatonicmysteries.common.lib;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.feature.world.processor.PsychonautHouseProcessor;
import com.miskatonicmysteries.common.lib.util.WorldUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.structure.rule.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class MMWorld {
    public static final StructureProcessorType<PsychonautHouseProcessor> PSYCHONAUT_PROCESSOR = StructureProcessorType.register(Constants.MOD_ID + ":psychonaut_house", PsychonautHouseProcessor.CODEC);

    public static final StructureProcessorList ZOMBIE_PROCESSOR = new StructureProcessorList(Arrays.asList(new PsychonautHouseProcessor(10091940), new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.8F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState()), new StructureProcessorRule(new TagMatchRuleTest(BlockTags.DOORS), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()), new StructureProcessorRule(new BlockMatchRuleTest(Blocks.TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()), new StructureProcessorRule(new BlockMatchRuleTest(Blocks.WALL_TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.07F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.MOSSY_COBBLESTONE, 0.07F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHITE_TERRACOTTA, 0.07F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_LOG, 0.05F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_PLANKS, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_STAIRS, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.STRIPPED_OAK_LOG, 0.02F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GLASS_PANE, 0.5F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule[]{new StructureProcessorRule(new BlockStateMatchRuleTest(Blocks.GLASS_PANE.getDefaultState().with(PaneBlock.NORTH, true).with(PaneBlock.SOUTH, true)), AlwaysTrueRuleTest.INSTANCE, Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState().with(PaneBlock.NORTH, true).with(PaneBlock.SOUTH, true)), new StructureProcessorRule(new BlockStateMatchRuleTest(Blocks.GLASS_PANE.getDefaultState().with(PaneBlock.EAST, true).with(PaneBlock.WEST, true)), AlwaysTrueRuleTest.INSTANCE, Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState().with(PaneBlock.EAST, true).with(PaneBlock.WEST, true))}))));
    public static final StructureProcessorList NORMAL_PROCESSOR = new StructureProcessorList(Arrays.asList(new PsychonautHouseProcessor(12081980), new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState())))));

    public static void init() {
        StructurePools.register(new StructurePool(new Identifier(Constants.MOD_ID, "village/common/hastur_cultist"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.method_30425(Constants.MOD_ID + ":village/common/hastur_cultist"), 1)), StructurePool.Projection.RIGID));
        StructurePools.register(new StructurePool(new Identifier(Constants.MOD_ID, "village/common/hastur_cultist_ascended"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.method_30425(Constants.MOD_ID + ":village/common/hastur_cultist_ascended"), 1)), StructurePool.Projection.RIGID));
    }

    public static StructurePool specialInject(StructurePool pool) {
        pool = WorldUtil.tryAddElementToPool(new Identifier("village/plains/houses"), pool, Constants.MOD_ID + ":village/plains/houses/psychonaut_house", StructurePool.Projection.RIGID, MiskatonicMysteries.config.world.psychonautHouseWeight, MMWorld.NORMAL_PROCESSOR);
        pool = WorldUtil.tryAddElementToPool(new Identifier("village/plains/zombie/houses"), pool, Constants.MOD_ID + ":village/plains/zombie/houses/psychonaut_house", StructurePool.Projection.RIGID, MiskatonicMysteries.config.world.psychonautHouseWeight, MMWorld.ZOMBIE_PROCESSOR);

        pool = WorldUtil.tryAddElementToPool(new Identifier("village/savanna/town_centers"), pool, Constants.MOD_ID + ":village/savanna/town_centers/shrine_1", StructurePool.Projection.RIGID, MiskatonicMysteries.config.world.hasturShrineWeight, MMWorld.NORMAL_PROCESSOR);
        return pool;
    }
}

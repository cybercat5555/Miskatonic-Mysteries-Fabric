package com.miskatonicmysteries.lib;

import com.google.common.collect.ImmutableList;
import com.miskatonicmysteries.common.CommonProxy;
import com.miskatonicmysteries.common.feature.world.processor.PsychonautHouseProcessor;
import com.miskatonicmysteries.lib.util.Constants;
import com.miskatonicmysteries.lib.util.WorldUtil;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.structure.rule.*;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Arrays;
import java.util.List;

public class ModWorld {
    public static final StructureProcessorType<PsychonautHouseProcessor> PSYCHONAUT_PROCESSOR = StructureProcessorType.register(Constants.MOD_ID + ":psychonaut_house", PsychonautHouseProcessor.CODEC);

    public static final PointOfInterestType PSYCHONAUT_POI = PointOfInterestHelper.register(new Identifier(Constants.MOD_ID, "psychonaut"), 1, 1, ModObjects.CHEMISTRY_SET);
    public static final List<StructureProcessor> ZOMBIE_PROCESSOR = Arrays.asList(new PsychonautHouseProcessor(10091940), new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.8F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState()), new StructureProcessorRule(new TagMatchRuleTest(BlockTags.DOORS), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()), new StructureProcessorRule(new BlockMatchRuleTest(Blocks.TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()), new StructureProcessorRule(new BlockMatchRuleTest(Blocks.WALL_TORCH), AlwaysTrueRuleTest.INSTANCE, Blocks.AIR.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.07F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.MOSSY_COBBLESTONE, 0.07F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHITE_TERRACOTTA, 0.07F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_LOG, 0.05F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_PLANKS, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.OAK_STAIRS, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.STRIPPED_OAK_LOG, 0.02F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GLASS_PANE, 0.5F), AlwaysTrueRuleTest.INSTANCE, Blocks.COBWEB.getDefaultState()), new StructureProcessorRule[]{new StructureProcessorRule(new BlockStateMatchRuleTest((BlockState) ((BlockState) Blocks.GLASS_PANE.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true)), AlwaysTrueRuleTest.INSTANCE, (BlockState) ((BlockState) Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState().with(PaneBlock.NORTH, true)).with(PaneBlock.SOUTH, true)), new StructureProcessorRule(new BlockStateMatchRuleTest((BlockState) ((BlockState) Blocks.GLASS_PANE.getDefaultState().with(PaneBlock.EAST, true)).with(PaneBlock.WEST, true)), AlwaysTrueRuleTest.INSTANCE, (BlockState) ((BlockState) Blocks.BROWN_STAINED_GLASS_PANE.getDefaultState().with(PaneBlock.EAST, true)).with(PaneBlock.WEST, true)), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.CARROTS.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.2F), AlwaysTrueRuleTest.INSTANCE, Blocks.POTATOES.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.WHEAT, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.BEETROOTS.getDefaultState())})));
    public static final List<StructureProcessor> NORMAL_PROCESSOR = Arrays.asList(new PsychonautHouseProcessor(10091940), new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.COBBLESTONE, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState()))));

    //Village-gen related code taken from https://github.com/FoundationGames/Sandwichable/tree/master/remappedSrc/io/github/foundationgames/sandwichable (credit goes to Foundationgames and Draylar)
    public static void init() {
        WorldUtil.addStructureToPool(new Identifier("village/plains/houses"), new Identifier(Constants.MOD_ID, "village/plains/houses/psychonaut_house"), CommonProxy.CONFIG.psychonautHouseWeight, NORMAL_PROCESSOR);
    }
}

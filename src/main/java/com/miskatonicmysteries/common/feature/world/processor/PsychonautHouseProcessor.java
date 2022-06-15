package com.miskatonicmysteries.common.feature.world.processor;

import com.miskatonicmysteries.common.registry.MMWorld;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;

public class PsychonautHouseProcessor extends StructureProcessor {

	public static final Codec<PsychonautHouseProcessor> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
		Codec.LONG.fieldOf("Seed").forGetter((psychonautHouseProcessor -> psychonautHouseProcessor.seed))
	).apply(builder, PsychonautHouseProcessor::new));

	public final static Block[] CARPETS = {Blocks.PURPLE_CARPET, Blocks.ORANGE_CARPET, Blocks.YELLOW_CARPET};
	private final Random random;
	private final long seed;

	public PsychonautHouseProcessor(long seed) {
		this.seed = seed;
		this.random = new Random(seed);
	}

	@Nullable
	@Override
	public Structure.StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos,
												Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2,
												StructurePlacementData structurePlacementData) {
		if (structureBlockInfo2.state.getBlock() instanceof CarpetBlock) {
			return new Structure.StructureBlockInfo(structureBlockInfo2.pos, CARPETS[random.nextInt(CARPETS.length)].getDefaultState(),
													structureBlockInfo2.nbt);
		} else if (structureBlockInfo2.state.getBlock() instanceof CandleBlock) {
			return new Structure.StructureBlockInfo(structureBlockInfo2.pos,
													structureBlockInfo2.state.with(CandleBlock.CANDLES, 1 + random.nextInt(4)),
													structureBlockInfo2.nbt);
		}
		return structureBlockInfo2;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return MMWorld.PSYCHONAUT_PROCESSOR;
	}
}

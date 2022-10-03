package com.miskatonicmysteries.common.feature.world.processor;

import com.miskatonicmysteries.common.registry.MMWorld;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import net.minecraft.util.math.random.Random;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PsychonautHouseProcessor extends StructureProcessor {

	public static final Codec<PsychonautHouseProcessor> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
		Codec.LONG.fieldOf("Seed").forGetter((psychonautHouseProcessor -> psychonautHouseProcessor.seed))
	).apply(builder, PsychonautHouseProcessor::new));

	public final static Block[] CARPETS = {Blocks.PURPLE_CARPET, Blocks.ORANGE_CARPET, Blocks.YELLOW_CARPET};
	private final Random random;
	private final long seed;

	public PsychonautHouseProcessor(long seed) {
		this.seed = seed;
		this.random = Random.create(seed);
	}



	@org.jetbrains.annotations.Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
		if (currentBlockInfo.state.getBlock() instanceof CarpetBlock) {
			return new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos, CARPETS[random.nextInt(CARPETS.length)].getDefaultState(),
					currentBlockInfo.nbt);
		} else if (currentBlockInfo.state.getBlock() instanceof CandleBlock) {
			return new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos,
					currentBlockInfo.state.with(CandleBlock.CANDLES, 1 + random.nextInt(4)),
					currentBlockInfo.nbt);
		}
		return currentBlockInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return MMWorld.PSYCHONAUT_PROCESSOR;
	}
}

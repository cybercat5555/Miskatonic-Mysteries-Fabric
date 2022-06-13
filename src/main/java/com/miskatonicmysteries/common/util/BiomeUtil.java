package com.miskatonicmysteries.common.util;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.mixin.biomes.ChunkSectionAccessor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

public class BiomeUtil {

	@Environment(EnvType.CLIENT)
	public static void syncBiomeClient(ClientWorld world, List<BlockPos> changedBlocks) {
		if (MiskatonicMysteries.config.client.forceChunkColorUpdates) {
			Set<ChunkPos> chunks = changedBlocks.stream().map(ChunkPos::new).collect(Collectors.toSet());
			for (ChunkPos chunkPos : chunks) {
				world.resetChunkColor(chunkPos);
				for (int k = world.getBottomSectionCoord(); k < world.getTopSectionCoord(); ++k) {
					world.scheduleBlockRenders(chunkPos.x, k, chunkPos.z);
				}
			}
		}
	}

	public static void updateBiomeColor(World world, List<BlockPos> changedBlocks) {
		if (MiskatonicMysteries.config.client.forceChunkColorUpdates && world.isClient) {
			ClientWorld clientWorld = (ClientWorld) world;
			Set<ChunkPos> chunks = changedBlocks.stream().map(ChunkPos::new).collect(Collectors.toSet());
			updateBiomeColor(clientWorld, chunks);
		}
	}

	@Environment(EnvType.CLIENT)
	public static void updateBiomeColor(ClientWorld clientWorld, Set<ChunkPos> changedChunks) {
		for (ChunkPos chunkPos : changedChunks) {
			clientWorld.resetChunkColor(chunkPos);
			for (int k = clientWorld.getBottomSectionCoord(); k < clientWorld.getTopSectionCoord(); ++k) {
				clientWorld.scheduleBlockRenders(chunkPos.x, k, chunkPos.z);
			}
		}
	}

	public static void setBiome(World world, BlockPos pos, RegistryEntry<Biome> biome) {
		setBiome(world, world.getWorldChunk(pos), BiomeCoords.fromBlock(pos.getX()), BiomeCoords.fromBlock(pos.getY()),
			BiomeCoords.fromBlock(pos.getZ()), biome);
	}

	public static void setBiome(World world, Chunk chunk, int biomeX, int biomeY, int biomeZ, RegistryEntry<Biome> biome) {
		int sectionIndex = world.getSectionIndex(BiomeCoords.toBlock(biomeY));
		if (sectionIndex < 0 || sectionIndex >= world.getTopSectionCoord()) {
			return;
		}
		ChunkSection section = chunk.getSection(sectionIndex);
		int sectionX = biomeX & 3;
		int sectionY = biomeY & 3;
		int sectionZ = biomeZ & 3;
		((ChunkSectionAccessor) section).getBiomeContainer().swap(sectionX, sectionY, sectionZ, biome);
		chunk.setNeedsSaving(true);
	}

	public static void revertBiome(World world, BlockPos pos) {
		revertBiome(world, world.getWorldChunk(pos),  BiomeCoords.fromBlock(pos.getX()), BiomeCoords.fromBlock(pos.getY()),
			BiomeCoords.fromBlock(pos.getZ()));
	}

	public static void revertBiome(World world, Chunk chunk, int biomeX, int biomeY, int biomeZ) {
		setBiome(world, chunk, biomeX, biomeY, biomeZ, world.getGeneratorStoredBiome(biomeX, biomeY, biomeZ));
	}
}

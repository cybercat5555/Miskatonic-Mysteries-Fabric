package com.miskatonicmysteries.common.feature.block.blockentity;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.api.block.ObeliskBlock;
import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState;
import com.miskatonicmysteries.common.feature.world.MMDimensionalWorldState.BiomeKnot;
import com.miskatonicmysteries.common.feature.world.biome.BiomeEffect;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.SyncBiomeReversionPacket;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.util.BiomeUtil;
import com.miskatonicmysteries.common.util.Constants.NBT;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;

public class ObeliskBlockEntity extends BaseBlockEntity implements Affiliated {

	private static final int RANGE = 8;
	public int ticks;

	public ObeliskBlockEntity(BlockPos pos, BlockState state) {
		super(MMObjects.HASTUR_OBELISK_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static void tick(ObeliskBlockEntity obelisk) {
		BiomeEffect effect = MiskatonicMysteriesAPI.getBiomeEffect(obelisk.world, obelisk.getPos());
		if (effect != null && effect.getAffiliation(false) == obelisk.getAffiliation(false)) {
			if (obelisk.ticks <= 640) {
				obelisk.ticks++;
				if (obelisk.ticks % 20 == 0) {
					World world = obelisk.getWorld();
					BlockPos pos = obelisk.getPos();
					RegistryEntry<Biome> biome = world.getBiome(pos);
					changeBiomes(obelisk, world, pos, biome);
				}
			}
		} else {
			obelisk.ticks = 0;
		}
	}

	private static void changeBiomes(ObeliskBlockEntity obelisk, World world, BlockPos pos, RegistryEntry<Biome> biome) {
		List<BlockPos> changedPoses = new ArrayList<>();
		int range = Math.min(obelisk.ticks / 20, RANGE);
		int biomeX = BiomeCoords.fromBlock(pos.getX());
		int biomeY = BiomeCoords.fromBlock(pos.getY());
		int biomeZ = BiomeCoords.fromBlock(pos.getZ());
		for (int x = -range; x < range; x++) {
			for (int z = -range; z < range; z++) {
				for (int y = -4; y < 4; y++) {
					BlockPos changedPos = pos.add(x * 4, y * 4, z * 4);
					BiomeUtil.setBiome(world, world.getWorldChunk(changedPos), biomeX + x, biomeY + y, biomeZ + z, biome);
					changedPoses.add(changedPos);
				}
			}
		}
		BiomeUtil.updateBiomeColor(world, changedPoses);
		if (world instanceof ServerWorld serverWorld) {
			MMDimensionalWorldState.get(serverWorld).setBiomeKnot(pos, range * 4, true, false);
		}
	}

	public static void revertChangedBiomes(World world, BlockPos pos) {
		if (world instanceof ServerWorld serverWorld) {
			List<BlockPos> changedPoses = new ArrayList<>();
			MMDimensionalWorldState worldState = MMDimensionalWorldState.get(serverWorld);

			worldState.setBiomeKnot(pos, -1, false, false);
			List<BiomeKnot> knots = worldState.getNearbyKnots(pos, RANGE);
			int biomeX = BiomeCoords.fromBlock(pos.getX());
			int biomeY = BiomeCoords.fromBlock(pos.getY());
			int biomeZ = BiomeCoords.fromBlock(pos.getZ());
			for (int x = -RANGE; x < RANGE; x++) {
				for (int z = -RANGE; z < RANGE; z++) {
					for (int y = -4; y < 4; y++) {
						BlockPos changedPos = pos.add(x * 4, y * 4, z * 4);
						if (!intersectsKnot(changedPos, knots)) {
							BiomeUtil.revertBiome(world, world.getWorldChunk(changedPos), biomeX + x, biomeY + y, biomeZ + z);
							changedPoses.add(changedPos);
						}
					}
				}
			}
			PlayerLookup.tracking(serverWorld, pos).forEach(player -> SyncBiomeReversionPacket.send(player, changedPoses));
		}
	}

	private static boolean intersectsKnot(BlockPos changedPos, List<BiomeKnot> knots) {
		for (BiomeKnot knot : knots) {
			if (knot.isCore() && changedPos.getSquaredDistance(knot.getPos()) < (Math.pow(knot.getRadius(), 2))) {
				return true;
			} else if (!knot.isCore() && (changedPos.getX() < (knot.getPos().getX() + knot.getRadius())
				&& changedPos.getX() > knot.getPos().getX() - knot.getRadius()) && (
				changedPos.getY() < (knot.getPos().getY() + knot.getRadius()) && changedPos.getY() > knot.getPos().getY() - knot
					.getRadius()) && (changedPos.getZ() < (knot.getPos().getZ() + knot.getRadius())
				&& changedPos.getZ() > knot.getPos().getZ() - knot.getRadius())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		tag.putInt(NBT.TICK_COUNT, ticks);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		ticks = tag.getInt(NBT.TICK_COUNT);
		super.readNbt(tag);
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return getCachedState().getBlock() instanceof ObeliskBlock o ? o.getAffiliation(apparent) : MMAffiliations.NONE;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}
}

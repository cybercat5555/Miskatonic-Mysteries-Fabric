package com.miskatonicmysteries.common.feature.world;

import static com.miskatonicmysteries.common.util.Constants.NBT.ACTIVE;
import static com.miskatonicmysteries.common.util.Constants.NBT.CENTER_POS;
import static com.miskatonicmysteries.common.util.Constants.NBT.KNOTS;
import static com.miskatonicmysteries.common.util.Constants.NBT.RADIUS;
import static com.miskatonicmysteries.common.util.Constants.NBT.WARDING_MARKS;

import com.miskatonicmysteries.api.MiskatonicMysteriesAPI;
import com.miskatonicmysteries.common.util.Constants;
import com.miskatonicmysteries.common.util.Constants.NBT;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.biome.source.BiomeCoords;

public class MMDimensionalWorldState extends PersistentState {

	private final Set<BlockPos> wardingMarks = new HashSet<>();
	private final HashMap<BlockPos, BiomeMaskKnot> maskKnots = new HashMap<>();

	public static MMDimensionalWorldState fromNbt(NbtCompound tag) {
		MMDimensionalWorldState state = new MMDimensionalWorldState();
		NbtList wardingMarksList = tag.getList(WARDING_MARKS, NbtElement.COMPOUND_TYPE);
		if (wardingMarksList != null) {
			for (NbtElement blockTag : wardingMarksList) {
				state.wardingMarks.add(NbtHelper.toBlockPos((NbtCompound) blockTag));
			}
		}
		NbtList knotList = tag.getList(KNOTS, NbtElement.COMPOUND_TYPE);
		if (knotList != null) {
			for (NbtElement knot : knotList) {
				BiomeMaskKnot maskKnot = BiomeMaskKnot.fromNbt((NbtCompound) knot);
				state.maskKnots.put(maskKnot.root, maskKnot);
			}
		}
		return state;
	}

	public static MMDimensionalWorldState get(ServerWorld world) {
		return world.getPersistentStateManager()
			.getOrCreate(MMDimensionalWorldState::fromNbt, MMDimensionalWorldState::new, Constants.MOD_ID + "_dimensional");
	}

	public void addKnot(BlockPos pos, int radius) {
		maskKnots.compute(pos, (root, knot) -> {
			if (knot == null) {
				return new BiomeMaskKnot(true, root, radius);
			}
			if (knot.radius < radius) {
				knot.radius = radius;
			}
			return knot;
		});
		markDirty();
	}

	public void deactivateKnot(BlockPos pos) {
		if (maskKnots.containsKey(pos)) {
			maskKnots.get(pos).active = false;
		}
		markDirty();
	}
	public void addMark(BlockPos markPos) {
		wardingMarks.add(markPos);
		markDirty();
	}

	public void removeMark(BlockPos markPos) {
		wardingMarks.remove(markPos);
		markDirty();
	}

	public boolean isMarkNear(BlockPos pos, int radius) {
		for (BlockPos wardingMark : wardingMarks) {
			if (wardingMark.isWithinDistance(pos, radius)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		NbtList wardingMarksList = new NbtList();
		for (BlockPos wardingMark : wardingMarks) {
			wardingMarksList.add(NbtHelper.fromBlockPos(wardingMark));
		}
		tag.put(WARDING_MARKS, wardingMarksList);
		NbtList knotList = new NbtList();
		for (BiomeMaskKnot maskKnot : maskKnots.values()) {
			knotList.add(maskKnot.writeNbt());
		}
		tag.put(KNOTS, knotList);

		return tag;
	}

	private static class BiomeMaskKnot {
		public boolean active;
		public BlockPos root;
		public int radius;

		private final Iterator<BlockPos> blocks;

		private BiomeMaskKnot(boolean active, BlockPos root, int radius) {
			this.active = active;
			this.root = root;
			this.radius = radius;

			blocks = BlockPos.iterateOutwards(root, radius, radius, radius).iterator();
		}

		private static BiomeMaskKnot fromNbt(NbtCompound nbt) {
			boolean active = nbt.getBoolean(NBT.ACTIVE);
			BlockPos root = NbtHelper.toBlockPos(nbt.getCompound(CENTER_POS));
			int radius = nbt.getInt(RADIUS);
			return new BiomeMaskKnot(active, root, radius);
		}

		private NbtCompound writeNbt(){
			NbtCompound compound = new NbtCompound();
			compound.putBoolean(ACTIVE, active);
			compound.put(CENTER_POS, NbtHelper.fromBlockPos(root));
			compound.putInt(RADIUS, radius);
			return compound;
		}

		private boolean revert(ServerWorld world) {
			if (!blocks.hasNext()) {
				return true;
			}
			BlockPos pos = blocks.next();
			MiskatonicMysteriesAPI.setBiome(world, pos, world.getGeneratorStoredBiome(
				BiomeCoords.fromBlock(pos.getX()),
				BiomeCoords.fromBlock(pos.getY()),
				BiomeCoords.fromBlock(pos.getZ())
			));
			return false;
		}
	}
}

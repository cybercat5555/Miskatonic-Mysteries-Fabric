package com.miskatonicmysteries.common.feature.world.party;

import com.miskatonicmysteries.common.util.Constants;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

public class MMPartyState extends PersistentState {

	private final Map<Integer, Party> parties = new HashMap<>();
	private ServerWorld world;
	private int nextAvailableId;
	private int tickCount;

	public static MMPartyState get(ServerWorld world) {
		return world.getPersistentStateManager().getOrCreate(nbtCompound -> MMPartyState.fromNbt(world, nbtCompound),
															 MMPartyState::new, Constants.MOD_ID + "parties");
	}

	public static MMPartyState fromNbt(ServerWorld world, NbtCompound tag) {
		MMPartyState state = new MMPartyState();
		state.world = world;
		state.nextAvailableId = tag.getInt(Constants.NBT.NEXT_ID);
		state.tickCount = tag.getInt(Constants.NBT.TICK_COUNT);
		NbtList partyList = tag.getList(Constants.NBT.PARTIES, 10);
		for (int i = 0; i < partyList.size(); i++) {
			Party party = new Party(world, (NbtCompound) partyList.get(i));
			state.parties.put(party.getId(), party);
		}
		return state;
	}

	public void tick() {
		tickCount++;
		Iterator<Party> iterator = this.parties.values().iterator();

		while (iterator.hasNext()) {
			Party party = iterator.next();
			if (party.shouldStop()) {
				party.concludeParty();
				iterator.remove();
				this.markDirty();
			} else {
				party.tick();
			}
		}

		if (tickCount % 200 == 0) {
			this.markDirty();
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.putInt(Constants.NBT.NEXT_ID, this.nextAvailableId);
		tag.putInt(Constants.NBT.TICK_COUNT, this.tickCount);
		NbtList nbtList = new NbtList();
		for (Party party : parties.values()) {
			NbtCompound nbtCompound = new NbtCompound();
			party.writeNbt(nbtCompound);
			nbtList.add(nbtCompound);
		}
		tag.put(Constants.NBT.PARTIES, nbtList);
		return tag;
	}

	public boolean tryStartParty(ServerWorld world, BlockPos blockPos) {
		if (getParty(blockPos) == null) {
			Party party = new Party(world, blockPos, nextId());
			parties.put(party.getId(), party);
			return true;
		}
		return false;
	}

	public Party getParty(BlockPos pos) {
		return getPartyAt(pos, 9216);
	}

	@Nullable
	public Party getPartyAt(BlockPos pos, int searchDistance) {
		for (Party party : parties.values()) {
			if (party.getCenter().getSquaredDistance(pos) < searchDistance) {
				return party;
			}
		}
		return null;
	}

	public int nextId() {
		return ++this.nextAvailableId;
	}
}

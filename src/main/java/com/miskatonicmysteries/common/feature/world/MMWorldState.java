package com.miskatonicmysteries.common.feature.world;

import com.miskatonicmysteries.common.feature.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.miskatonicmysteries.common.util.Constants.NBT.*;

public class MMWorldState extends PersistentState {
    private final Map<UUID, ProtagonistEntity.ProtagonistData> protagonistMap = new HashMap<>();
    private final Map<UUID, Boolean> houndMap = new HashMap<>(); //uuid - player; boolean - is the hound in-world?

    public static MMWorldState fromNbt(NbtCompound tag) {
        MMWorldState state = new MMWorldState();
        NbtList protagonistList = (NbtList) tag.get(PROTAGONISTS);
        if (protagonistList != null) {
            for (NbtElement baseTag : protagonistList) {
                NbtCompound compoundTag = (NbtCompound) baseTag;
                state.protagonistMap
                        .put(compoundTag.getUuid(PLAYER_UUID), ProtagonistEntity.ProtagonistData.fromTag(compoundTag));
            }
        }
        NbtList houndList = (NbtList) tag.get(HOUNDS);
        if (houndList != null) {
            for (NbtElement baseTag : houndList) {
                NbtCompound compoundTag = (NbtCompound) baseTag;
                state.houndMap.put(compoundTag.getUuid(PLAYER_UUID), compoundTag.getBoolean(SPAWNED));
            }
        }
        return state;
    }

    public static MMWorldState get(World world) {
        return world.getServer().getOverworld().getPersistentStateManager()
                .getOrCreate(MMWorldState::fromNbt, MMWorldState::new, Constants.MOD_ID);
    }

    public void addProtagonist(PlayerEntity player, ProtagonistEntity.ProtagonistData data) {
        protagonistMap.put(player.getUuid(), data);
        markDirty();
    }

    public void removeProtagonist(ProtagonistEntity protagonist) {
        if (protagonist.getTargetUUID().isPresent()) {
            protagonistMap.remove(protagonist.getTargetUUID().get());
        }
        markDirty();
    }

    public ProtagonistEntity.ProtagonistData getProtagonistDataFor(PlayerEntity player) {
        if (!protagonistMap.containsKey(player.getUuid())) {
            ProtagonistHandler.createProtagonist(player.world, player);
        }
        return protagonistMap.get(player.getUuid());
    }

    public ProtagonistEntity.ProtagonistData getProtagonistDataFor(ProtagonistEntity protagonist) {
        if (!protagonist.getTargetUUID().isPresent() || !protagonistMap
                .containsKey(protagonist.getTargetUUID().get())) {
            return null;
        }
        return protagonistMap.get(protagonist.getTargetUUID().get());
    }

    public void markHoundState(UUID player, boolean spawned) {
        this.houndMap.put(player, spawned);
        markDirty();
    }

    public void removeHoundForUUID(UUID player) {
        this.houndMap.remove(player);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        NbtList protagonistList = new NbtList();
        protagonistMap.forEach((uuid, protagonistData) -> {
            NbtCompound compoundTag = new NbtCompound();
            compoundTag.putUuid(PLAYER_UUID, uuid);
            protagonistData.toTag(compoundTag);
            protagonistList.add(compoundTag);
        });
        tag.put(PROTAGONISTS, protagonistList);

        NbtList houndList = new NbtList();
        houndMap.forEach(((uuid, spawned) -> {
            NbtCompound compoundTag = new NbtCompound();
            compoundTag.putUuid(PLAYER_UUID, uuid);
            compoundTag.putBoolean(SPAWNED, spawned);
            houndList.add(compoundTag);
        }));
        tag.put(HOUNDS, houndList);
        return tag;
    }

    public Text clear() {
        protagonistMap.clear();
        markDirty();
        return new TranslatableText("miskatonicmysteries.command.clear_data");
    }

    public boolean getHoundState(UUID uuid) {
        return houndMap.getOrDefault(uuid, true);
    }
}

package com.miskatonicmysteries.common.feature.world;

import com.miskatonicmysteries.common.block.YellowSignBlock;
import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;

import static com.miskatonicmysteries.common.util.Constants.NBT.*;

public class MMWorldState extends PersistentState {
    private final Map<UUID, ProtagonistEntity.ProtagonistData> protagonistMap = new HashMap<>();
    private final Map<YellowSignBlock.VillageMarker, BlockPos> markedVillages = new HashMap<>();

    public MMWorldState() {
        super(Constants.MOD_ID);
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
        if (!protagonist.getTargetUUID().isPresent() || !protagonistMap.containsKey(protagonist.getTargetUUID().get())) {
            return null;
        }
        return protagonistMap.get(protagonist.getTargetUUID().get());
    }

    public void markVillage(BlockPos villagePos, YellowSignBlock.VillageMarker marker) {
        markedVillages.put(marker, villagePos);
        markDirty();
    }

    public void unmarkVillage(BlockPos markerPos) {
        for (YellowSignBlock.VillageMarker villageMarker : markedVillages.keySet()) {
            if (villageMarker.markerBlockPos.equals(markerPos)) {
                markedVillages.remove(villageMarker);
                markDirty();
                break;
            }
        }
    }

    public void unmarkVillages(UUID player) {
        for (YellowSignBlock.VillageMarker villageMarker : markedVillages.keySet()) {
            if (villageMarker.player.equals(player)) {
                markedVillages.remove(villageMarker);
            }
        }
        markDirty();
    }

    public Collection<BlockPos> getMarkedVillages() {
        return markedVillages.values();
    }

    public int getUniquelyMarkedVillages(PlayerEntity player) {
        int counter = 0;
        List<BlockPos> villages = new ArrayList<>();
        for (YellowSignBlock.VillageMarker villageMarker : markedVillages.keySet()) {
            if (villageMarker.player.equals(player.getUuid()) && !villages.contains(markedVillages.get(villageMarker))) {
                counter++;
                villages.add(markedVillages.get(villageMarker));
            }
        }
        return counter;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag protagonistList = (ListTag) tag.get(PROTAGONISTS);
        if (protagonistList != null) {
            for (Tag baseTag : protagonistList) {
                CompoundTag compoundTag = (CompoundTag) baseTag;
                protagonistMap.put(compoundTag.getUuid(PLAYER_UUID), ProtagonistEntity.ProtagonistData.fromTag(compoundTag));

            }
        }

        ListTag markerList = (ListTag) tag.get(MARKED_VILLAGES);
        if (markerList != null) {
            for (Tag baseTag : markerList) {
                CompoundTag compoundTag = (CompoundTag) baseTag;
                markedVillages.put(new YellowSignBlock.VillageMarker(compoundTag.getUuid(PLAYER_UUID), BlockPos.fromLong(compoundTag.getLong(MARKER_POS))), BlockPos.fromLong(compoundTag.getLong(VILLAGE_POS)));
            }
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag protagonistList = new ListTag();
        protagonistMap.forEach((uuid, protagonistData) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUuid(PLAYER_UUID, uuid);
            protagonistData.toTag(compoundTag);
            protagonistList.add(compoundTag);
        });
        tag.put(PROTAGONISTS, protagonistList);

        ListTag markerList = new ListTag();
        markedVillages.forEach((marker, pos) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putLong(VILLAGE_POS, pos.asLong());
            compoundTag.putUuid(PLAYER_UUID, marker.player);
            compoundTag.putLong(MARKER_POS, marker.markerBlockPos.asLong());
            markerList.add(compoundTag);
        });
        tag.put(MARKED_VILLAGES, markerList);
        return tag;
    }

    public static MMWorldState get(World world) {
        return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(() -> new MMWorldState(), Constants.MOD_ID);
    }


    public Text clear() {
        protagonistMap.clear();
        markedVillages.clear();
        markDirty();
        return new TranslatableText("miskatonicmysteries.command.clear_data");
    }



}

package com.miskatonicmysteries.common.feature.world;

import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.miskatonicmysteries.common.util.Constants.NBT.PLAYER_UUID;
import static com.miskatonicmysteries.common.util.Constants.NBT.PROTAGONISTS;

public class MMWorldState extends PersistentState {
    private final Map<UUID, ProtagonistEntity.ProtagonistData> protagonistMap = new HashMap<>();
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

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag protagonistList = (ListTag) tag.get(PROTAGONISTS);
        if (protagonistList != null) {
            for (Tag baseTag : protagonistList) {
                CompoundTag compoundTag = (CompoundTag) baseTag;
                protagonistMap.put(compoundTag.getUuid(PLAYER_UUID), ProtagonistEntity.ProtagonistData.fromTag(compoundTag));

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

        return tag;
    }

    public static MMWorldState get(World world) {
        return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(() -> new MMWorldState(), Constants.MOD_ID);
    }


    public Text clear() {
        protagonistMap.clear();
        markDirty();
        return new TranslatableText("miskatonicmysteries.command.clear_data");
    }



}

package com.miskatonicmysteries.common.feature.world;

import com.miskatonicmysteries.common.entity.ProtagonistEntity;
import com.miskatonicmysteries.common.handler.ProtagonistHandler;
import com.miskatonicmysteries.common.lib.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.miskatonicmysteries.common.lib.Constants.NBT.PLAYER_UUID;
import static com.miskatonicmysteries.common.lib.Constants.NBT.PROTAGONISTS;

public class MMWorldState extends PersistentState {
    private final Map<UUID, ProtagonistEntity.ProtagonistData> PROTAGONIST_MAP = new HashMap<>();
    //   private final Map<String, Set<Pair<Identifier, BlockPos>>> TELEPORTS = new ConcurrentHashMap<>(); might not even do teleports in world data lol

    public MMWorldState() {
        super(Constants.MOD_ID);
    }

    /* public void addTeleport(String channel, BlockPos pos, ServerWorld world) {
         if (!TELEPORTS.containsKey(channel)){
             TELEPORTS.put(channel, new HashSet<>());
         }
         TELEPORTS.get(channel).add(new Pair<>(world.getRegistryKey().getValue(), pos));
         markDirty();
     }

     public void removeTeleport(BlockPos pos, ServerWorld world) {
         for (String key : TELEPORTS.keySet()) {
             TELEPORTS.get(key).remove(new Pair<>(world.getRegistryKey().getValue(), pos));
         }
         markDirty();
     }
 */
    public void addProtagonist(PlayerEntity player, ProtagonistEntity.ProtagonistData data) {
        PROTAGONIST_MAP.put(player.getUuid(), data);
        markDirty();
    }

    public void removeProtagonist(ProtagonistEntity protagonist) {
        if (protagonist.getTargetUUID().isPresent())
            PROTAGONIST_MAP.remove(protagonist.getTargetUUID().get());
        markDirty();
    }

    public ProtagonistEntity.ProtagonistData getProtagonistDataFor(PlayerEntity player) {
        if (!PROTAGONIST_MAP.containsKey(player.getUuid())) ProtagonistHandler.createProtagonist(player.world, player);
        return PROTAGONIST_MAP.get(player.getUuid());
    }

    public ProtagonistEntity.ProtagonistData getProtagonistDataFor(ProtagonistEntity protagonist) {
        if (!protagonist.getTargetUUID().isPresent() || !PROTAGONIST_MAP.containsKey(protagonist.getTargetUUID().get()))
            return null;
        return PROTAGONIST_MAP.get(protagonist.getTargetUUID().get());
    }

    @Override
    public void fromTag(CompoundTag tag) {
        ListTag protagonistList = (ListTag) tag.get(PROTAGONISTS);
        protagonistList.forEach(baseTag -> {
            CompoundTag compoundTag = (CompoundTag) baseTag;
            PROTAGONIST_MAP.put(compoundTag.getUuid(PLAYER_UUID), ProtagonistEntity.ProtagonistData.fromTag(compoundTag));
        });

       /* TELEPORTS.clear();
        ListTag locationsList = (ListTag) tag.get(CHANNELS);
        locationsList.forEach(baseTag -> {
            CompoundTag compoundTag = (CompoundTag) baseTag;
            Set<Pair<Identifier, BlockPos>> loc = new HashSet<>();
            compoundTag.getList(LOCATIONS, 10).forEach((location) -> {
                CompoundTag locationCompound = (CompoundTag) location;
                loc.add(new Pair<>(new Identifier(locationCompound.getString(DIMENSION)), BlockPos.fromLong(locationCompound.getLong(POSITION))));
            });
            TELEPORTS.put(compoundTag.getString(CHANNEL), loc);
        });*/
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        ListTag protagonistList = new ListTag();
        PROTAGONIST_MAP.forEach((uuid, protagonistData) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putUuid(PLAYER_UUID, uuid);
            protagonistData.toTag(compoundTag);
            protagonistList.add(compoundTag);
        });
        tag.put(PROTAGONISTS, protagonistList);

      /*  ListTag teleportList = new ListTag();
        TELEPORTS.forEach((channel, locations) -> {
            CompoundTag compoundTag = new CompoundTag();
            ListTag locationsList = new ListTag();
            locations.forEach(pair -> {
                CompoundTag locationsTag = new CompoundTag();
                locationsTag.putString(DIMENSION, pair.getFirst().toString());
                locationsTag.putLong(POSITION, pair.getSecond().asLong());
                locationsList.add(locationsTag);
            });
            compoundTag.putString(CHANNEL, channel);
            compoundTag.put(LOCATIONS, locationsList);
            teleportList.add(compoundTag);
        });
        tag.put(CHANNELS, teleportList);*/
        return tag;
    }

    public static MMWorldState get(World world) {
        return Objects.requireNonNull(Objects.requireNonNull(world.getServer()).getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(() -> new MMWorldState(), Constants.MOD_ID);
    }

    /* public Set<Pair<Identifier, BlockPos>> getGates(String channel) {
         return TELEPORTS.containsKey(channel) ? TELEPORTS.get(channel) : new HashSet<>();
     }
 */
    public Text clear() {
        PROTAGONIST_MAP.clear();
        // TELEPORTS.clear();
        markDirty();
        return new TranslatableText("miskatonicmysteries.command.clear_data");
    }
}

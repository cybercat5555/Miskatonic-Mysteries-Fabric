package com.miskatonicmysteries.api.interfaces;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;
import java.util.UUID;

public interface OthervibeMobEntityAccessor {

	boolean access(PlayerEntity player);

	Optional<UUID> getData(MobEntity mobEntity);

	void setData(Optional<UUID> mobEntity);
}

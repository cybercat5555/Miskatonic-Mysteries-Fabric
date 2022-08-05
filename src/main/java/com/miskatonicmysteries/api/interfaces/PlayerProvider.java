package com.miskatonicmysteries.api.interfaces;

import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;

public interface PlayerProvider {
	@Nullable PlayerEntity mm_getPlayer();
}

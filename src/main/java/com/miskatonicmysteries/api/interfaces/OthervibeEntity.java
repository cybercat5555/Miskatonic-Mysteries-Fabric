package com.miskatonicmysteries.api.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface OthervibeEntity {

	boolean isVisibleTo(PlayerEntity player);

	void setIsVisibleTo(PlayerEntity player);
}

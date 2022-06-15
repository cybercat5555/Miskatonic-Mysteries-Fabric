package com.miskatonicmysteries.mixin.entity;

import net.minecraft.entity.mob.ZombieVillagerEntity;

import java.util.UUID;

import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombieVillagerEntity.class)
public interface ZombieVillagerAccessor {

	@Invoker
	void callSetConverting(@Nullable UUID uuid, int delay);
}

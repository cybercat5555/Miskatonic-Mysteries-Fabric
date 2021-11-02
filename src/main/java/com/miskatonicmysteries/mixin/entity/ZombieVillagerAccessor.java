package com.miskatonicmysteries.mixin.entity;

import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombieVillagerEntity.class)
public interface ZombieVillagerAccessor {

	@Invoker
	void callSetConverting(@Nullable UUID uuid, int delay);
}

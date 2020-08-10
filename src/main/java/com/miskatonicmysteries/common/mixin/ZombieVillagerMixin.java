package com.miskatonicmysteries.common.mixin;

import net.minecraft.entity.mob.ZombieVillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ZombieVillagerEntity.class)
public interface ZombieVillagerMixin {
    @Invoker("setConverting(Ljava/util/UUID;I)V")
    void callSetConverting(@Nullable UUID uuid, int delay);
}

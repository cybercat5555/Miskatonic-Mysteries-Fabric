package com.miskatonicmysteries.mixin.villagers;

import net.minecraft.entity.ai.brain.MemoryModuleType;

import com.mojang.serialization.Codec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MemoryModuleType.class)
public interface MemoryModuleTypeAccessor {

	@Invoker("register")
	static <U> MemoryModuleType<U> invokeRegister(String id, Codec<U> codec) {
		throw new AssertionError();
	}
}

package com.miskatonicmysteries.mixin.villagers;

import net.minecraft.entity.ai.brain.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Activity.class)
public interface ActivityAccessor {

	@Invoker("register")
	static Activity invokeRegister(String id) {
		throw new AssertionError();
	}
}

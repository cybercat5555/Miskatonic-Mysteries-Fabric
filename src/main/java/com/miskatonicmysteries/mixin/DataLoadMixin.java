package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.handler.DataHandler;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourceManager.class)
public abstract class DataLoadMixin {

	@Shadow
	@Final
	private ReloadableResourceManager resourceManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void addToConstructor(DynamicRegistryManager registryManager, CommandManager.RegistrationEnvironment commandEnvironment,
		int functionPermissionLevel, CallbackInfo info) {
		this.resourceManager.registerReloader(new DataHandler());
	}
}
package com.miskatonicmysteries.mixin;

import com.miskatonicmysteries.common.handler.DataHandler;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ServerResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResourceManager.class)
public abstract class DataLoadMixin{
    @Shadow private ReloadableResourceManager resourceManager;
    @Inject(method = "<init>(Lnet/minecraft/server/command/CommandManager$RegistrationEnvironment;I)V", at = @At("TAIL"))
    private void addToConstructor(CallbackInfo info){
        this.resourceManager.registerReloader(new DataHandler());
    }
}
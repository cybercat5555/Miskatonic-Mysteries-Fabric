package com.miskatonicmysteries.mixin.client;

import com.miskatonicmysteries.client.render.ShaderHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final private Map<String, Shader> shaders;

    @Inject(method = "loadShaders", at = @At("TAIL"))
    private void loadShaders(ResourceManager manager, CallbackInfo ci) {
        ShaderHandler.loadShaders(manager, shaders);
    }
}

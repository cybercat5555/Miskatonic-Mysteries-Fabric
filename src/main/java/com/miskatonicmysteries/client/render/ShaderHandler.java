package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.client.ClientProxy;
import com.miskatonicmysteries.lib.ModRegistries;
import com.miskatonicmysteries.lib.util.Constants;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderHandler {
    private static final ManagedShaderEffect MANIA = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/mania.json"));

    public static void init(){
        ShaderEffectRenderCallback.EVENT.register(parTick -> {
            if (ClientProxy.CONFIG.useShaders && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(ModRegistries.MANIA)){
                MANIA.render(parTick);
            }
        });
    }
}


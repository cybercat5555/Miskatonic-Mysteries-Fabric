package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.lib.Constants;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderHandler {
    private static final ManagedShaderEffect MANIA = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/mania.json"));
    public static boolean enableMania = false;

    public static void init(){
        ShaderEffectRenderCallback.EVENT.register(parTick -> {
            if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.isSneaking()) enableMania = true;
            if (enableMania){
                MANIA.render(parTick);
                enableMania = false; //must be enabled each tick
            }
        });
    }
}


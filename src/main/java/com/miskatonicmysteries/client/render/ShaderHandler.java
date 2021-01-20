package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMMiscRegistries;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderHandler {
    private static final ManagedShaderEffect MANIA = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/mania.json"));

    public static void init() {
        ShaderEffectRenderCallback.EVENT.register(parTick -> {
            if (MiskatonicMysteries.config.client.useShaders && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(MMMiscRegistries.StatusEffects.MANIA)) {
                MANIA.render(parTick);
            }
        });
    }
}


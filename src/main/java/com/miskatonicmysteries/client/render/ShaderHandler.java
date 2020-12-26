package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.client.MiskatonicMysteriesClient;
import com.miskatonicmysteries.lib.ModRegistries;
import com.miskatonicmysteries.lib.util.Constants;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderHandler {
    private static final ManagedShaderEffect MANIA = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/mania.json"));

    //todo re-enable stuff in MMBook, and re-add "patchouli": ">=1.16-38-FABRIC", to mod.json, as well as "cotton": ">=1.0.5" and config stuff
    public static void init() {
        ShaderEffectRenderCallback.EVENT.register(parTick -> {
            if (MiskatonicMysteriesClient.config.useShaders && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(ModRegistries.MANIA)) {
                MANIA.render(parTick);
            }
        });
    }
}


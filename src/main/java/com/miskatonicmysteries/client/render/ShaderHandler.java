package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.lib.Constants;
import com.miskatonicmysteries.common.lib.MMMiscRegistries;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform3f;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;

public class ShaderHandler implements ShaderEffectRenderCallback, ClientTickEvents.EndTick {
    private static final ManagedShaderEffect MANIA = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/mania.json"));
    private static final Uniform3f MANIA_UNIFORM_3_F = MANIA.findUniform3f("Phosphor");

    public void init() {
        ClientTickEvents.END_CLIENT_TICK.register(this);
        ShaderEffectRenderCallback.EVENT.register(this);
    }

    @Override
    public void renderShaderEffects(float v) {
        if (MiskatonicMysteries.config.client.useShaders && MinecraftClient.getInstance().player != null) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player.hasStatusEffect(MMMiscRegistries.StatusEffects.MANIA)) {
                MANIA.render(v);
            }
        }
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player != null) {
            if (client.player.hasStatusEffect(MMMiscRegistries.StatusEffects.MANIA)
                    && client.player.getStatusEffect(MMMiscRegistries.StatusEffects.MANIA).getAmplifier() > 0) {
                MANIA_UNIFORM_3_F.set(0.9F, 0.8F, 0.8F);
            } else {
                MANIA_UNIFORM_3_F.set(0.8F, 0.7F, 0.7F);
            }
        }
    }
}


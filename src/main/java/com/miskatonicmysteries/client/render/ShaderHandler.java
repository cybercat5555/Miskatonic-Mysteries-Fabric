package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.registry.MMMiscRegistries;
import com.miskatonicmysteries.common.util.Constants;
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
    private static final ManagedShaderEffect RESONANCE = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/resonance.json"));
    private static final Uniform3f MANIA_PHOSPHOR = MANIA.findUniform3f("Phosphor");
    private static final Uniform3f RESONANCE_RED = RESONANCE.findUniform3f("RedMatrix");
    private static final Uniform3f RESONANCE_GREEN = RESONANCE.findUniform3f("GreenMatrix");
    private static final Uniform3f RESONANCE_BLUE = RESONANCE.findUniform3f("BlueMatrix");

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
            Resonating.of(player).ifPresent(resonating -> {
                if (resonating.getResonance() > 0) {
                    RESONANCE.render(v);
                }
            });
        }
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (MiskatonicMysteries.config.client.useShaders && client.player != null) {
            if (client.player.hasStatusEffect(MMMiscRegistries.StatusEffects.MANIA)
                    && client.player.getStatusEffect(MMMiscRegistries.StatusEffects.MANIA).getAmplifier() > 0) {
                MANIA_PHOSPHOR.set(0.9F, 0.8F, 0.8F);
            } else {
                MANIA_PHOSPHOR.set(0.8F, 0.7F, 0.7F);
            }
            Resonating.of(client.player).ifPresent(resonating -> {
                RESONANCE_RED.set(1, resonating.getResonance() * 0.75F, resonating.getResonance() * 0.75F);
                RESONANCE_GREEN.set(0, 1, 0);
                RESONANCE_BLUE.set(resonating.getResonance(), resonating.getResonance(), 1);
            });
        }
    }
}


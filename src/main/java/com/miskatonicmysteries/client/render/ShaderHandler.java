package com.miskatonicmysteries.client.render;

import com.miskatonicmysteries.api.interfaces.Resonating;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.registry.MMStatusEffects;
import com.miskatonicmysteries.common.util.Constants;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import ladysnake.satin.api.managed.uniform.Uniform3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ShaderHandler {
    public static final ManagedShaderEffect MANIA = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/mania.json"));
    public static final ManagedShaderEffect RESONANCE = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/resonance.json"));
    public static final ManagedShaderEffect CLAIRVOYANCE = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/clairvoyance.json"));
    private static final Uniform3f MANIA_PHOSPHOR = MANIA.findUniform3f("Phosphor");
    private static final Uniform3f RESONANCE_RED = RESONANCE.findUniform3f("RedMatrix");
    private static final Uniform3f RESONANCE_GREEN = RESONANCE.findUniform3f("GreenMatrix");
    private static final Uniform3f RESONANCE_BLUE = RESONANCE.findUniform3f("BlueMatrix");
    private static final Uniform1f CLAIRVOYANCE_SATURATION = CLAIRVOYANCE.findUniform1f("Saturation");
    private static final Uniform1f CLAIRVOYANCE_BLUR = CLAIRVOYANCE.findUniform1f("Threshold");

    public static int clairvoyanceTime;
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(ShaderHandler::onEndTick);
        ShaderEffectRenderCallback.EVENT.register(ShaderHandler::renderShaderEffects);
    }

    public static void renderShaderEffects(float v) {
        if (MiskatonicMysteries.config.client.useShaders && MinecraftClient.getInstance().player != null) {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            if (player.hasStatusEffect(MMStatusEffects.MANIA)) {
                MANIA.render(v);
            }
            Resonating.of(player).ifPresent(resonating -> {
                if (resonating.getResonance() > 0) {
                    RESONANCE.render(v);
                }
            });
            if (clairvoyanceTime > 0) {
                CLAIRVOYANCE.render(v);
            }
        }
    }

    public static void onEndTick(MinecraftClient client) {
        if (MiskatonicMysteries.config.client.useShaders && client.player != null) {
            if (client.player.hasStatusEffect(MMStatusEffects.MANIA)
                    && client.player.getStatusEffect(MMStatusEffects.MANIA).getAmplifier() > 0) {
                MANIA_PHOSPHOR.set(0.9F, 0.8F, 0.8F);
            } else {
                MANIA_PHOSPHOR.set(0.8F, 0.7F, 0.7F);
            }
            Resonating.of(client.player).ifPresent(resonating -> {
                RESONANCE_RED.set(1, resonating.getResonance() * 0.75F, resonating.getResonance() * 0.75F);
                RESONANCE_GREEN.set(0, 1, 0);
                RESONANCE_BLUE.set(resonating.getResonance(), resonating.getResonance(), 1);
            });

            if (client.player.hasStatusEffect(MMStatusEffects.CLAIRVOYANCE)) {
                if (clairvoyanceTime < 100) {
                    clairvoyanceTime++;
                }
            }else if (clairvoyanceTime > 0){
                clairvoyanceTime--;
            }

            if (clairvoyanceTime >= 0) {
                CLAIRVOYANCE_SATURATION.set(0.95F - MathHelper.clamp(clairvoyanceTime / 100F, 0F, 0.7F));
                CLAIRVOYANCE_BLUR.set(1 - MathHelper.clamp(clairvoyanceTime / 100F, 0F, 0.7F));
            }
        }
    }
}


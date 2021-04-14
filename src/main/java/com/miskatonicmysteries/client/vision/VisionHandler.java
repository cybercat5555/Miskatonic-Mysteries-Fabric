package com.miskatonicmysteries.client.vision;

import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class VisionHandler {
    public static final Map<Identifier, VisionSequence> VISIONS = new HashMap<>();
    private static VisionSequence sequence;
    public static void init(){
        register(new Identifier(Constants.MOD_ID, "hastur_bless"), new HasturBlessingVision());
        HudRenderCallback.EVENT.register(VisionHandler::tick);
        //proper render event registry
    }

    private static void tick(MatrixStack stack, float tickDelta){
        MinecraftClient client = MinecraftClient.getInstance();
        if (sequence != null && client.player != null){
            sequence.render(client, client.player, stack, tickDelta);
        }
    }

    public static void setVisionSequence(ClientPlayerEntity player, VisionSequence sequence) {
        if (sequence == null){
            VisionHandler.sequence = null;
            return;
        }
        if (VisionHandler.sequence != sequence) {
            VisionHandler.sequence = sequence;
            sequence.onStart(player);
        }
    }

    public static VisionSequence getCurrentSequence(){
        return VisionHandler.sequence;
    }

    public static VisionSequence register(Identifier id, VisionSequence sequence){
        return VISIONS.put(id, sequence);
    }

    public static VisionSequence getSequence(Identifier id) {
        return VISIONS.getOrDefault(id, null);
    }
}

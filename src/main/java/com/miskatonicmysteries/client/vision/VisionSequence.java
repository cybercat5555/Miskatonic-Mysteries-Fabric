package com.miskatonicmysteries.client.vision;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public abstract class VisionSequence extends DrawableHelper {

	protected int ticks;

	public abstract void render(MinecraftClient client, ClientPlayerEntity player, MatrixStack stack, float tickDelta);

	public void onStart(ClientPlayerEntity player) {
		ticks = 0;
	}
}

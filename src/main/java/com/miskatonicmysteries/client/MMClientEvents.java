package com.miskatonicmysteries.client;

import com.miskatonicmysteries.client.sound.ResonatorSound;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public class MMClientEvents {
	public static void init(){
		ClientTickEvents.END_CLIENT_TICK.register(MMClientEvents::handleSounds);
	}

	private static void handleSounds(MinecraftClient minecraftClient) {
		for (BlockPos blockPos : ResonatorSound.soundInstances.keySet()) {
			if (ResonatorSound.soundInstances.get(blockPos).isDone()) {
				ResonatorSound.soundInstances.remove(blockPos);
			}
		}
	}
}

package com.miskatonicmysteries.api.registry;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.common.handler.networking.packet.s2c.InsanityEventPacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class InsanityEvent {

	public final float baseChance;
	public final int insanityThreshold;
	private final Identifier id;

	public InsanityEvent(Identifier id, float baseChance, int insanityThreshold) {
		this.id = id;
		this.baseChance = baseChance;
		this.insanityThreshold = insanityThreshold;
	}

	public Identifier getId() {
		return id;
	}

	public boolean execute(PlayerEntity playerEntity, Sanity sanity) {
		if (!playerEntity.world.isClient) {
			InsanityEventPacket.send(playerEntity, id);
		}
		return true;
	}

	public boolean test(PlayerEntity player, Sanity sanity, float insanityFactor) {
		return sanity.getSanity() <= insanityThreshold && player.getRandom().nextFloat() < baseChance;
	}
}

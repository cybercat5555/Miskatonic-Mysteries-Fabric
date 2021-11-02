package com.miskatonicmysteries.common.handler;

import static com.miskatonicmysteries.common.util.Constants.DataTrackers.SANITY_CAP;

import com.miskatonicmysteries.api.interfaces.Sanity;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.common.MiskatonicMysteries;
import com.miskatonicmysteries.common.registry.MMObjects;
import com.miskatonicmysteries.common.registry.MMRegistries;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InsanityHandler {

	public static boolean hasSanityCapExpansion(PlayerEntity player, String expansion) {
		Optional<Sanity> sanity = Sanity.of(player);
		return sanity.isPresent() && sanity.get().getSanityCapExpansions().containsKey(expansion);
	}

	public static void handleInsanityEvents(PlayerEntity player) {
		Optional<Sanity> optionalSanity = Sanity.of(player);
		float insanityFactor = 1F - calculateSanityFactor(optionalSanity);
		optionalSanity.ifPresent(sanity -> {
			if (player.getRandom().nextFloat() < (0.1F + (0.1F * insanityFactor))) {
				InsanityEvent event = findInsanityEvent(player, sanity, insanityFactor);
				if (event != null) {
					event.execute(player, (Sanity) player);
				}
			}
		});
	}

	private static InsanityEvent findInsanityEvent(PlayerEntity player, Sanity sanity, float insanityFactor) {
		List<InsanityEvent> events = MMRegistries.INSANITY_EVENTS.stream().filter(event -> event.test(player, sanity, insanityFactor))
			.collect(Collectors.toList());
		for (int i = 0; i < MiskatonicMysteries.config.sanity.insanityEventAttempts; i++) {
			if (events.isEmpty()) {
				return null;
			}
			InsanityEvent event = events.get(player.getRandom().nextInt(events.size()));
			if (player.getRandom().nextFloat() < event.baseChance) {
				return event;
			}
			events.remove(event);
		}
		return null;
	}

	public static float calculateSanityFactor(Optional<Sanity> sanity) {
		return sanity.map(value -> value.getSanity() / (float) SANITY_CAP).orElse(1F);
	}

	public static void handleClientSideBlockChange(ClientPlayerEntity player, World world, BlockState state, BlockPos pos, Random random) {
		Sanity.of(player).ifPresent(sanity -> {
			if (sanity.getSanity() < 750 && random.nextFloat() < 0.2F) {
				if (state.getBlock().equals(Blocks.BIRCH_LOG)) {
					world.setBlockState(pos, MMObjects.BIRCH_LOG.getDefaultState(), 1);
				}
			}
		});
	}
}

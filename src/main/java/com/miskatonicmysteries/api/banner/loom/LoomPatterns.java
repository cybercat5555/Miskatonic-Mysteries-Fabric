package com.miskatonicmysteries.api.banner.loom;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import static com.miskatonicmysteries.api.banner.MMBannerRegistry.LOOM_PATTERN_REGISTRY;

/**
 * API location of the loom pattern registry.
 */
public final class LoomPatterns {

	/**
	 * The registry key for custom banner patterns, called Loom Patterns.
	 */

	@SuppressWarnings("unchecked")
	public static final RegistryKey<Registry<LoomPattern>> REGISTRY_KEY = (RegistryKey<Registry<LoomPattern>>) LOOM_PATTERN_REGISTRY.getKey();
	/**
	 * The registry for custom banner patterns, called Loom Patterns.
	 */
	public static final Registry<LoomPattern> REGISTRY = LOOM_PATTERN_REGISTRY;

	private LoomPatterns() {
	}
}
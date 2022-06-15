package com.miskatonicmysteries.api.banner.impl;

import com.miskatonicmysteries.api.banner.loom.LoomPattern;
import com.miskatonicmysteries.api.banner.loom.LoomPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * Loom pattern utility methods.
 */
public final class LoomPatternsInternal {

	private static final List<LoomPattern> nonSpecialPatterns = new ArrayList<>();
	private static final List<LoomPattern> specialPatterns = new ArrayList<>();

	private LoomPatternsInternal() {
	}

	/**
	 * Returns the ID used to represent this loom pattern in the loom container and screen. Loom indices allow the loom to still reference custom
	 * patterns by index without non-trivial calculations in order to skip special loom patterns.
	 */
	public static int getLoomIndex(LoomPattern pattern) {
		if (pattern.isSpecial()) {
			return specialPatterns.indexOf(pattern) + nonSpecialPatterns.size();
		} else {
			return nonSpecialPatterns.indexOf(pattern);
		}
	}

	/**
	 * Returns the loom pattern represented by the given loom index.
	 */
	public static LoomPattern byLoomIndex(int loomIndex) {
		if (loomIndex < nonSpecialPatterns.size()) {
			return nonSpecialPatterns.get(loomIndex);
		} else {
			return specialPatterns.get(loomIndex - nonSpecialPatterns.size());
		}
	}

	/**
	 * Returns the number of dye (non-special) loom patterns. This number is also the loom index of the first special loom pattern.
	 */
	public static int dyeLoomPatternCount() {
		return nonSpecialPatterns.size();
	}

	/**
	 * Returns the total number of loom patterns. This number is also the number of entries in the loom pattern registry, but it is easier to obtain
	 * using this method.
	 */
	public static int totalLoomPatternCount() {
		return nonSpecialPatterns.size() + specialPatterns.size();
	}

	/**
	 * Called every time the registries are synced to rebuild the loom index table. Loom pattern indices are based on the order in which loom patterns
	 * appear in the registry, not on the raw IDs themselves, hence why we don't use the remap context provided by Fabric API. User code should not
	 * call this method, as it will have no effect in normal gameplay.
	 */
	public static void remapLoomIndices() {
		nonSpecialPatterns.clear();
		specialPatterns.clear();

		for (LoomPattern p : LoomPatterns.REGISTRY) {
			addPattern(p);
		}
	}

	/**
	 * Adds a single loom pattern to the loom pattern index. Called in the registry entry addition callback, so patterns are guaranteed to be added in
	 * raw ID order.
	 */
	public static void addPattern(LoomPattern pattern) {
		if (!pattern.isSpecial()) {
			nonSpecialPatterns.add(pattern);
		} else {
			specialPatterns.add(pattern);
		}
	}
}
package com.miskatonicmysteries.api.banner.loom;

/**
 * Implement this on an Item to mark it as a pattern item.
 *
 * @see io.github.fablabsmc.fablabs.api.bannerpattern.v1.LoomPatternItem for a convenience implementation.
 */
public interface LoomPatternProvider {

	/**
	 * @return The pattern associated with this item. Must not be null.
	 */
	LoomPattern getPattern();
}
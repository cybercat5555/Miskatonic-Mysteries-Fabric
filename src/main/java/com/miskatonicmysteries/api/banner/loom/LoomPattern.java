package com.miskatonicmysteries.api.banner.loom;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * An extensible version of BannerPattern. Instances are referenced by their registry ID.
 */
public class LoomPattern {

	private final boolean special;

	public LoomPattern(boolean special) {
		this.special = special;
	}

	/**
	 * @throws NullPointerException if {@code patternId} does not refer to a registered LoomPattern.
	 * @deprecated Acquire a {@link LoomPattern} instance and call {@link #getSpriteId(String)} directly.
	 */
	@Deprecated
	public static Identifier getSpriteId(Identifier patternId, String namespace) {
		return LoomPatterns.REGISTRY.get(patternId).getSpriteId(namespace);
	}

	/**
	 * @param type Context where this pattern is applied, e.g. <code>shield</code> or <code>banner</code>.
	 * @return the sprite ID for the pattern mask texture for this LoomPattern.
	 * @throws NullPointerException if this {@code LoomPattern} has not been registered.
	 */
	public Identifier getSpriteId(String type) {
		Identifier myId = LoomPatterns.REGISTRY.getId(this);
		return new Identifier(myId.getNamespace(), "pattern/" + type + "/" + myId.getPath());
	}

	/**
	 * Whether this loom pattern requires an item in the pattern slot.
	 */
	public final boolean isSpecial() {
		return special;
	}

	/**
	 * Adds a description of this LoomPattern's appearance to {@code lines}.
	 *
	 * @param color The color this pattern has been dyed with.
	 * @throws NullPointerException if this {@code LoomPattern} has not been registered.
	 */
	public void addPatternLine(List<Text> lines, DyeColor color) {
		Identifier id = LoomPatterns.REGISTRY.getId(this);
		lines.add(new TranslatableText(
			"bannerpp.pattern." + id.getNamespace() + "." + id.getPath() + "." + color.getName())
					  .formatted(Formatting.GRAY));
	}
}
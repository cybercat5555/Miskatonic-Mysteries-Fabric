package com.miskatonicmysteries.api.banner.loom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The Banner++ equivalent of BannerPatternItem.
 */
public class LoomPatternItem extends Item implements LoomPatternProvider {

	private final LoomPattern pattern;

	public LoomPatternItem(LoomPattern pattern, Item.Settings settings) {
		super(settings);
		this.pattern = checkNotNull(pattern);
	}

	@Override
	public final LoomPattern getPattern() {
		return pattern;
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> lines, TooltipContext ctx) {
		lines.add(getDescription().formatted(Formatting.GRAY));
	}

	public MutableText getDescription() {
		return new TranslatableText(this.getTranslationKey() + ".desc");
	}
}
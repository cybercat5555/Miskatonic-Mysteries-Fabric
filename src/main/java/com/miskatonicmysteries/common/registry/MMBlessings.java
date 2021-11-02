package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.common.feature.blessing.MagicBoostBlessing;
import com.miskatonicmysteries.common.feature.blessing.RoyalEntourageBlessing;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMBlessings {

	public static final Blessing CHARMING_PERSONALITY = new Blessing(new Identifier(Constants.MOD_ID, "charming_personality"),
		(apparent) -> MMAffiliations.HASTUR);
	public static final Blessing ROYAL_ENTOURAGE = new RoyalEntourageBlessing();
	public static final Blessing MAGIC_BOOST = new MagicBoostBlessing();
	public static final Blessing BNUUY = new Blessing(new Identifier(Constants.MOD_ID, "fuckin_bnuuy"),
		(apparent) -> MMAffiliations.HASTUR);

	public static void init() {
		register(CHARMING_PERSONALITY);
		register(ROYAL_ENTOURAGE);
		register(MAGIC_BOOST);
		register(BNUUY);
	}

	private static void register(Blessing blessing) {
		Registry.register(MMRegistries.BLESSINGS, blessing.getId(), blessing);
	}
}

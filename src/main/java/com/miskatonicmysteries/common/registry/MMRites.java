package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.common.feature.block.blockentity.OctagramBlockEntity;
import com.miskatonicmysteries.common.feature.recipe.instability_event.EntropyInstabilityEvent;
import com.miskatonicmysteries.common.feature.recipe.instability_event.InstabilityEvent;
import com.miskatonicmysteries.common.feature.recipe.instability_event.MobsInstabilityEvent;
import com.miskatonicmysteries.common.feature.recipe.instability_event.PotionInstabilityEvent;
import com.miskatonicmysteries.common.feature.recipe.instability_event.SpellEffectInstabilityEvent;
import com.miskatonicmysteries.common.feature.recipe.rite.BrokenVeilRite;
import com.miskatonicmysteries.common.feature.recipe.rite.BurnedVeilRite;
import com.miskatonicmysteries.common.feature.recipe.rite.GoldenFlockRite;
import com.miskatonicmysteries.common.feature.recipe.rite.HasturBiomeRite;
import com.miskatonicmysteries.common.feature.recipe.rite.HysteriaRite;
import com.miskatonicmysteries.common.feature.recipe.rite.MasterpieceRite;
import com.miskatonicmysteries.common.feature.recipe.rite.MourningDeadRite;
import com.miskatonicmysteries.common.feature.recipe.rite.SculptorRite;
import com.miskatonicmysteries.common.feature.recipe.rite.TeleportRite;
import com.miskatonicmysteries.common.feature.recipe.rite.summon.ByakheeSummoningRite;
import com.miskatonicmysteries.common.feature.recipe.rite.summon.PrinceSummoningRite;

import net.minecraft.util.registry.Registry;

public class MMRites {

	public static final Rite OPEN_WAY = new TeleportRite();
	public static final Rite BURNED_VEIL = new BurnedVeilRite();
	public static final Rite BROKEN_VEIL = new BrokenVeilRite();
	public static final Rite HYSTERIA = new HysteriaRite();
	public static final Rite SCULPTOR_RITE = new SculptorRite();
	public static final Rite GOLDEN_FLOCK_RITE = new GoldenFlockRite();
	public static final Rite SUMMON_PRINCE_RITE = new PrinceSummoningRite();
	public static final Rite SUMMON_BYAKHEE = new ByakheeSummoningRite();
	public static final Rite MOURNING_DEAD_RITE = new MourningDeadRite();

	public static final Rite HASTUR_BIOME_RITE = new HasturBiomeRite();

	public static final Rite MASTERPIECE_RITE = new MasterpieceRite();

	public static void init() {
		register(OPEN_WAY);
		register(BURNED_VEIL);
		register(BROKEN_VEIL);
		register(HYSTERIA);
		register(SCULPTOR_RITE);
		register(GOLDEN_FLOCK_RITE);
		register(SUMMON_PRINCE_RITE);
		register(SUMMON_BYAKHEE);
		register(MOURNING_DEAD_RITE);

		register(HASTUR_BIOME_RITE);
		register(MASTERPIECE_RITE);

		registerInstabilityEvent(new EntropyInstabilityEvent());
		registerInstabilityEvent(new PotionInstabilityEvent());
		registerInstabilityEvent(new MobsInstabilityEvent());
		registerInstabilityEvent(new SpellEffectInstabilityEvent());
	}

	private static void register(Rite rite) {
		Registry.register(MMRegistries.RITES, rite.getId(), rite);
	}

	private static void registerInstabilityEvent(InstabilityEvent event) {
		Registry.register(MMRegistries.INSTABILITY_EVENTS, event.getId(), event);
	}

	public static Rite getRite(OctagramBlockEntity octagram) {
		return MMRegistries.RITES.stream().filter(r -> r.canCast(octagram)).findFirst().orElse(null);
	}
}

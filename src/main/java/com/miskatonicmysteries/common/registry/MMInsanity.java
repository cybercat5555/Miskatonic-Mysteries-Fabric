package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.api.registry.InsanityInducer;
import com.miskatonicmysteries.common.feature.sanity.ExoticCravingsInsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.SpawnHallucinationInsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.SpookySoundInsanityEvent;
import com.miskatonicmysteries.common.feature.sanity.UltraViolenceInsanityEvent;
import com.miskatonicmysteries.common.util.Constants;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMInsanity {

	public static final InsanityEvent SPOOKY_SOUND = new SpookySoundInsanityEvent();
	public static final InsanityEvent EXOTIC_CRAVINGS = new ExoticCravingsInsanityEvent();
	public static final InsanityEvent HALLUCINATIONS = new SpawnHallucinationInsanityEvent();
	public static final InsanityEvent ULTRA_VIOLENCE = new UltraViolenceInsanityEvent();

	public static final InsanityInducer BASE_INDUCERS = new InsanityInducer(new Identifier(Constants.MOD_ID, "base_insanity_inducers"),
		20, false, true, true, Ingredient.ofItems(MMObjects.BLOTTER));

	public static void init() {
		registerEvent(SPOOKY_SOUND);
		registerEvent(EXOTIC_CRAVINGS);
		registerEvent(HALLUCINATIONS);
		registerEvent(ULTRA_VIOLENCE);

		registerInducer(BASE_INDUCERS);
	}

	private static void registerEvent(InsanityEvent event) {
		Registry.register(MMRegistries.INSANITY_EVENTS, event.getId(), event);
	}

	private static void registerInducer(InsanityInducer event) {
		Registry.register(MMRegistries.INSANITY_INDUCERS, event.getId(), event);
	}

}

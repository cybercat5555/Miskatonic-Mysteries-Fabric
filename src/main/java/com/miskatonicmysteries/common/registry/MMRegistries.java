package com.miskatonicmysteries.common.registry;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.api.registry.Blessing;
import com.miskatonicmysteries.api.registry.InsanityEvent;
import com.miskatonicmysteries.api.registry.InsanityInducer;
import com.miskatonicmysteries.api.registry.Rite;
import com.miskatonicmysteries.api.registry.SpellEffect;
import com.miskatonicmysteries.api.registry.SpellMedium;
import com.miskatonicmysteries.common.feature.recipe.instability_event.InstabilityEvent;
import com.miskatonicmysteries.common.util.Constants;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MMRegistries {

	public static final Registry<Affiliation> AFFILIATIONS = FabricRegistryBuilder
		.createSimple(Affiliation.class, new Identifier(Constants.MOD_ID, "affiliations")).buildAndRegister();
	public static final Registry<Blessing> BLESSINGS = FabricRegistryBuilder
		.createSimple(Blessing.class, new Identifier(Constants.MOD_ID, "blessings")).buildAndRegister();
	public static final Registry<InsanityEvent> INSANITY_EVENTS = FabricRegistryBuilder
		.createSimple(InsanityEvent.class, new Identifier(Constants.MOD_ID, "insanity_events")).buildAndRegister();
	public static final Registry<InsanityInducer> INSANITY_INDUCERS = FabricRegistryBuilder
		.createSimple(InsanityInducer.class, new Identifier(Constants.MOD_ID, "insanity_inducers")).buildAndRegister();
	public static final Registry<InstabilityEvent> INSTABILITY_EVENTS = FabricRegistryBuilder
		.createSimple(InstabilityEvent.class, new Identifier(Constants.MOD_ID, "instability_event")).buildAndRegister();
	public static final Registry<Rite> RITES = FabricRegistryBuilder.createSimple(Rite.class, new Identifier(Constants.MOD_ID, "rites"))
		.buildAndRegister();
	public static final Registry<SpellEffect> SPELL_EFFECTS = FabricRegistryBuilder
		.createSimple(SpellEffect.class, new Identifier(Constants.MOD_ID, "spell_effects")).buildAndRegister();
	public static final Registry<SpellMedium> SPELL_MEDIUMS = FabricRegistryBuilder
		.createSimple(SpellMedium.class, new Identifier(Constants.MOD_ID, "spell_mediums")).buildAndRegister();
}

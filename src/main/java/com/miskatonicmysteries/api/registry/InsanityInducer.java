package com.miskatonicmysteries.api.registry;

import com.miskatonicmysteries.api.interfaces.Sanity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class InsanityInducer {

	private final Identifier id;
	public final int sanityPenalty;
	public final boolean decreasesSanityCap;
	public final boolean withShockFactor;
	public final Ingredient ingredient;
	public final boolean ignoreFactors;

	public InsanityInducer(Identifier id, int sanityPenalty, boolean decreasesSanityCap, boolean withShockFactor, boolean ignoreFactors,
		Ingredient ingredient) {
		this.id = id;
		this.sanityPenalty = sanityPenalty;
		this.decreasesSanityCap = decreasesSanityCap;
		this.withShockFactor = withShockFactor;
		this.ignoreFactors = ignoreFactors;
		this.ingredient = ingredient;
	}

	public void induceInsanity(World world, LivingEntity entity, Sanity sanity) {
		if (!world.isClient) {
			sanity.setSanity(sanity.getSanity() - sanityPenalty, ignoreFactors);
			if (decreasesSanityCap) {
				sanity.addSanityCapExpansion(id.toString(), -sanityPenalty);
			}
			if (withShockFactor) {
				sanity.setShocked(true);
			}
			sanity.syncSanityData();
		}
	}

	public Identifier getId() {
		return id;
	}
}

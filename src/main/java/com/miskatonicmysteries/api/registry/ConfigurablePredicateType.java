package com.miskatonicmysteries.api.registry;

import com.miskatonicmysteries.common.handler.predicate.ConfigurablePredicate;

import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ConfigurablePredicateType {

	public final Supplier<ConfigurablePredicate> predicateProvider;
	private final Identifier id;
	private final Identifier[] categories;

	public ConfigurablePredicateType(Identifier id, Supplier<ConfigurablePredicate> predicateProvider, Identifier... categories) {
		this.id = id;
		this.predicateProvider = predicateProvider;
		this.categories = categories;
	}

	public Identifier[] getCategories() {
		return categories;
	}

	public ConfigurablePredicate getPredicate() {
		return predicateProvider.get();
	}

	public Identifier getId() {
		return id;
	}
}

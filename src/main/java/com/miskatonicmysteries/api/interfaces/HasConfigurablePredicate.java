package com.miskatonicmysteries.api.interfaces;

import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.common.handler.predicate.ConfigurablePredicate;
import com.miskatonicmysteries.common.registry.MMCriteria;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;

public interface HasConfigurablePredicate {

	ConfigurablePredicate getPredicate();

	void setPredicate(ConfigurablePredicate config);

	@Environment(EnvType.CLIENT)
	default void openConfigurationScreen() {
		MinecraftClient.getInstance().setScreen(new ConfigurePredicateScreen(this, getPredicate(), MMCriteria.TARGET_PREDICATE));
	}

	@Environment(EnvType.CLIENT)
	void finishConfiguration(ConfigurablePredicate configured);
}

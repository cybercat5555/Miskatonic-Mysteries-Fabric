package com.miskatonicmysteries.common.handler.predicate;

import com.miskatonicmysteries.api.registry.ConfigurablePredicateType;
import com.miskatonicmysteries.client.gui.ConfigurePredicateScreen;
import com.miskatonicmysteries.common.registry.MMRegistries;
import com.miskatonicmysteries.common.util.Constants.NBT;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.function.Predicate;

public abstract class ConfigurablePredicate implements Predicate<Entity> {
	public final ConfigurablePredicateType type;

	protected ConfigurablePredicate(ConfigurablePredicateType type) {
		this.type = type;
	}

	@Override
	public abstract boolean test(Entity entity);

	public ConfigurablePredicate readFromNbt(NbtCompound compound) {
		return this;
	}

	public static ConfigurablePredicate fromNbt(NbtCompound compound) {
		ConfigurablePredicateType type = MMRegistries.CONFIGURABLE_PREDICATES.get(new Identifier(compound.getString(NBT.TYPE)));
		if (type != null) {
			return type.predicateProvider.get().readFromNbt(compound);
		}
		return null;
	}

	@Environment(EnvType.CLIENT)
	public void addWidgets(ConfigurePredicateScreen screen, Identifier currentCategory) {

	}

	public ConfigurablePredicate copy() {
		return fromNbt(writeNbt(new NbtCompound()));
	}

	public NbtCompound writeNbt(NbtCompound compound) {
		compound.putString(NBT.TYPE, type.getId().toString());
		return compound;
	}
}

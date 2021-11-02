package com.miskatonicmysteries.common.feature.world.biome;

import com.miskatonicmysteries.api.interfaces.Affiliated;
import com.miskatonicmysteries.api.registry.Affiliation;
import net.minecraft.entity.LivingEntity;

public class BiomeEffect implements Affiliated {

	private final Affiliation affiliation;

	public BiomeEffect(Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	public void tickFor(LivingEntity entity) {

	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return affiliation;
	}

	@Override
	public boolean isSupernatural() {
		return true;
	}
}

package com.miskatonicmysteries.api.interfaces;

import com.miskatonicmysteries.api.registry.Affiliation;

import java.util.Optional;

public interface Affiliated {

	static Optional<Affiliated> of(Object context) {
		if (context instanceof Affiliated) {
			return Optional.of(((Affiliated) context));
		}
		return Optional.empty();
	}

	Affiliation getAffiliation(boolean apparent);

	boolean isSupernatural();
}

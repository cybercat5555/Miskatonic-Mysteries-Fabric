package com.miskatonicmysteries.api.interfaces;

import com.miskatonicmysteries.api.registry.Affiliation;
import java.util.Optional;

public interface MalleableAffiliated extends Affiliated {

	static Optional<MalleableAffiliated> of(Object context) {
		if (context instanceof MalleableAffiliated) {
			return Optional.of(((MalleableAffiliated) context));
		}
		return Optional.empty();
	}

	void setAffiliation(Affiliation affiliation, boolean apparent);
}

package com.miskatonicmysteries.api.interfaces;

import java.util.List;
import java.util.Optional;

public interface Knowledge {

	static Optional<Knowledge> of(Object context) {
		if (context instanceof Knowledge) {
			return Optional.of(((Knowledge) context));
		}
		return Optional.empty();
	}

	void addKnowledge(String knowledge);

	boolean hasKnowledge(String knowledge);

	void clearKnowledge();

	void syncKnowledge();

	List<String> getKnowledge();
}

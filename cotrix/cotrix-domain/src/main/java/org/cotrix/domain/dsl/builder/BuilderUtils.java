package org.cotrix.domain.dsl.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.common.CommonUtils;
import org.cotrix.domain.trait.Identified;

public class BuilderUtils {

	static <S extends Identified.State, P extends Identified.Abstract<P, S>> Collection<S> reveal(
			Iterable<?> entities, Class<P> type) {

		Collection<S> states = new ArrayList<S>();

		for (Object a : entities)
			states.add(CommonUtils.reveal(a, type).state());

		return states;

	}

}

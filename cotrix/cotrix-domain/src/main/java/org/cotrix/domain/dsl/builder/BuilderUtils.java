package org.cotrix.domain.dsl.builder;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.common.Utils;
import org.cotrix.domain.trait.Identified;

public class BuilderUtils {

	static <S extends Identified.State, P extends Identified.Abstract<P, S>> Collection<S> reveal(
			Collection<?> entities, Class<P> type) {

		Collection<S> states = new ArrayList<S>();

		for (Object a : entities)
			states.add(Utils.reveal(a, type).state());

		return states;

	}

}

package org.cotrix.repository.impl.memory;

import org.cotrix.domain.trait.Named;
import org.cotrix.repository.Criterion;

public abstract class MCriteria {

	public static <T extends Named> Criterion<T> byName(Class<T> type) {
			
			return new MCriterion<T>() {
				
				public int compare(T o1, T o2) {
					return o1.name().getLocalPart().compareToIgnoreCase(o2.name().getLocalPart());
				};
			};
	}

}

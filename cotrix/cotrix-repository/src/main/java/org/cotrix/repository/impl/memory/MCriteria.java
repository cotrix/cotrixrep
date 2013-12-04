package org.cotrix.repository.impl.memory;

import org.cotrix.common.Utils;
import org.cotrix.repository.Criterion;

public class MCriteria {

	public static <T> Criterion<T> descending(final Criterion<T> c) {
		
		return new MCriterion<T>() {

			public int compare(T o1, T o2) {
				return (-1)*reveal(c).compare(o1,o2);
			};

		};
	}
	
	
	public static <T> Criterion<T> all(final Criterion<T> c1, final Criterion<T> c2) {

		return new MCriterion<T>() {

			public int compare(T o1, T o2) {
				int result = reveal(c1).compare(o1, o2);

				if (result == 0)
					result = reveal(c2).compare(o1, o2);

				return result;
			};

		};
	}
	@SuppressWarnings("all")
	private static <R> MCriterion<R> reveal(Criterion<R> criterion) {
		return Utils.reveal(criterion, MCriterion.class);
	}
}

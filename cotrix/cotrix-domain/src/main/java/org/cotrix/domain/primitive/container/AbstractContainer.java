package org.cotrix.domain.primitive.container;

import static org.cotrix.domain.trait.Change.*;
import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.trait.Change;
import org.cotrix.domain.trait.Copyable;
import org.cotrix.domain.trait.Mutable;

/**
 * Partial implementation of a {@link Mutable} and {@link Copyable} {@link Container}.
 * 
 * @author Fabio Simeoni
 * 
 * @param <T> the type of contained objects
 * @param <C> the type of the container itself
 */
public abstract class AbstractContainer<T> implements Container<T>, Mutable<Container<T>>, Copyable<Container<T>> {

	private Change change;

	@Override
	public Change change() {
		return change;
	}

	@Override
	public boolean isDelta() {
		return change != null;
	}

	@Override
	public void reset() {
		setChange(null);
	}

	public void setChange(Change change) {

		notNull(change);

		this.change = change;

	}

	// helper
	protected <S extends Mutable<S>> void propagateChangeFrom(S object) throws IllegalArgumentException {

		// redundant checks, but clearer

		// first time: inherit NEW or MODIFIED
		if (object.isDelta() && !this.isDelta())
			this.setChange(object.change() == NEW ? NEW : MODIFIED);

		// other times: if not another NEW, MODIFIED
		if (object.isDelta() && this.isDelta())
			if (object.change() != this.change)
				this.setChange(MODIFIED);

		if (this.isDelta() && !object.isDelta())
			throw new IllegalArgumentException("object is " + this.change + " and can only contain other changes");

	}

}

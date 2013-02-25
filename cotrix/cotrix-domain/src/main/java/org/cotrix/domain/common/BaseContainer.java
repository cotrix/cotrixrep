package org.cotrix.domain.common;

import static org.cotrix.domain.traits.Change.*;

import org.cotrix.domain.traits.Change;

public abstract class BaseContainer<T extends DomainObject<T>, C extends Container<T,C>> implements Container<T,C> {

	private Change change;
	
	public abstract boolean add(T object) throws IllegalArgumentException;
	
	@Override
	public Change change() {
		return change;
	}
	
	@Override
	public boolean isDelta() {
		return change!=null;
	}
	
	@Override
	public void reset() {
		setChange(null);
	}

	protected void setChange(Change status) {
		this.change=status;

	}
	
	// helper
	protected void handleDeltaOf(T object) throws IllegalArgumentException {

		// container becomes a delta if one of their elements is
		if (object.isDelta())
			setChange(MODIFIED);
		else
		// if it's a delta then it can only accept deltas
		if (this.isDelta())
			throw new IllegalArgumentException("cannot add object (" + object.id()
					+ "), the container already carries changes, but the object does not represent one");

	}
}

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

	public void setChange(Change status) {
		this.change=status;

	}
	
	// helper
	protected void handleDeltaOf(T object) throws IllegalArgumentException {
		
		if (this.isDelta())
			if (object.isDelta())
				if (this.change().canTransitionTo(object.change()))
					setChange(object.change());
				else
					throw new IllegalArgumentException("object is "+this.change+" and cannot become "+object.change());
			else
				throw new IllegalArgumentException("cannot add object (" + object.id()
						+ "), the container already carries changes, but the object does not represent one");
		else
			if (object.isDelta())
				this.setChange(object.change()==NEW?NEW:MODIFIED);

	}
}

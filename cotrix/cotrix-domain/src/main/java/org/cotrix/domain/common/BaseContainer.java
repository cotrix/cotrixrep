package org.cotrix.domain.common;

import static org.cotrix.domain.traits.Change.*;
import static org.cotrix.domain.utils.Utils.*;

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

	public void setChange(Change change) {
		
		notNull(change);
		
		this.change=change;

	}
	
	// helper
	protected void propagateChangeFrom(T object) throws IllegalArgumentException {
		
		//redundant checks, but clearer

		//first time: inherit NEW or MODIFIED 
		if (object.isDelta() && !this.isDelta())
			this.setChange(object.change()==NEW?NEW:MODIFIED);

		
		//other times: if not another NEW, MODIFIED
		if (object.isDelta() && this.isDelta()) 
			if (object.change()!=this.change)
				this.setChange(MODIFIED);
		

		if (this.isDelta() && !object.isDelta())
			throw new IllegalArgumentException("object is "+this.change+" and can only contain other changes");


	}
}

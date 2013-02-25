package org.cotrix.domain.pos;

import static org.cotrix.domain.traits.Change.*;
import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.DomainObject;
import org.cotrix.domain.traits.Change;
import org.cotrix.domain.traits.Mutable;
import org.cotrix.domain.utils.Utils;


/**
 * A partial implementation of parameter objects for {@link DomainObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class ObjectPO {

	private final String id;
	private QName name;
	private Change change;
	
	protected ObjectPO(String id) {
		
		//allowed to be null if such is policy for new objects
		this.id=id;;
	}
	
	/**
	 * Returns the identifier parameter.
	 * @return the identifier parameter
	 */
	public String id() {
		return id;
	}
	
	/**
	 * Returns the name parameter.
	 * @return the name parameter
	 */
	public QName name() {
		return name;
	}
	
	/**
	 * Sets the name parameter
	 * @param name the name parameter
	 */
	public void setName(QName name) {
		
		Utils.valid(name);
		
		this.name = name;
	}

	/**
	 * Returns the {@link Change} parameter.
	 * @return the parameter
	 */
	public Change change() {
		return change;
	}
	
	public boolean isDelta() {
		return change!=null;
	}

	/**
	 * Sets the {@link Change} parameter.
	 * @param the parameter
	 * @throws IllegalArgumentException if the parameter is null or is incompatible with the other parameters.
	 */
	public void setChange(Change change) throws IllegalArgumentException {
		
		valid(change);
		
		this.change = change;
	}

	private void valid(Change change) throws IllegalArgumentException {
		
		notNull("change",change);
		
		if (id()==null && change!=NEW)
			throw new IllegalArgumentException("object has no identifier hence cannot represent a change to an existing object");
		
		
		if (isDelta() && !this.change.canTransitionTo(change))
			throw new IllegalArgumentException("object is "+this.change+" and cannot become "+change);
	
	}
	
	protected void validateDeltaParameter(Mutable<?> parameter) throws IllegalArgumentException {
		
		if (parameter.isDelta())
			if (this.isDelta()){
				if (!this.change.canTransitionTo(parameter.change()))
					throw new IllegalArgumentException("object is "+this.change+" and cannot become "+change);
			}
			else
				this.setChange(parameter.change()==NEW?NEW:MODIFIED);
		else
			if (this.isDelta()) //deltas must be made of deltas
				throw new IllegalArgumentException("can only accepts parameters that represent changes");


	}
}

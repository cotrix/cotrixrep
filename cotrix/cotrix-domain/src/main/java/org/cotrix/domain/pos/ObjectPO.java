package org.cotrix.domain.pos;

import static org.cotrix.domain.traits.Change.*;
import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.primitives.DomainObject;
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
		
		notNull("change",change);

		//cannot change or delete without an identifier
		if (id()==null && change!=NEW)
			throw new IllegalArgumentException("object has no identifier hence cannot represent a change to an existing object");
		
		//cannot change once set
		if (this.isDelta() && this.change!=change)
			throw new IllegalArgumentException("object is "+this.change+" and cannot become "+change);

		
		this.change = change;
	}

	protected void propagateChangeFrom(Mutable<?> parameter) throws IllegalArgumentException {
		
		//NOTE: when we change the state, we do not pass through setter which would normally prevent overrides
		
		//first time: inherit NEW or MODIFIED 
		if (parameter.isDelta() && this.isDelta())
			if (parameter.change()!=this.change)
				this.change=MODIFIED;

		//other times: if not another NEW, MODIFIED
		if (this.isDelta() && !parameter.isDelta())
			throw new IllegalArgumentException("object is a delta ("+this.change+") and can only contain other changes");
		
		if (parameter.isDelta() && !this.isDelta())
			this.change=parameter.change()==NEW?NEW:MODIFIED;


	}
	
	
}

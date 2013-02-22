package org.cotrix.domain.pos;

import static org.cotrix.domain.common.Delta.*;
import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Delta;
import org.cotrix.domain.common.DomainObject;


/**
 * A partial implementation of parameter objects for {@link DomainObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class ObjectPO {

	private final String id;
	private QName name;
	private Delta delta;
	
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
		
		valid(name);
		
		this.name = name;
	}

	/**
	 * Returns the {@link Delta} parameter.
	 * @return the parameter
	 */
	public Delta delta() {
		return delta;
	}

	/**
	 * Sets the {@link Delta} parameter.
	 * @param the parameter
	 */
	public void setDelta(Delta status) {
		
		if (status!=NEW && id()==null)
			throw new IllegalArgumentException("object has no identifier hence cannot be updated or deleted");
			this.delta = status;
	}
}

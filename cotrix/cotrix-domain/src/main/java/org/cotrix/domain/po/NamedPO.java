package org.cotrix.domain.po;

import javax.xml.namespace.QName;

import org.cotrix.domain.primitive.entity.NamedEntity;
import org.cotrix.domain.utils.Utils;


/**
 * A partial implementation of parameter objects for {@link NamedEntity}s.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class NamedPO extends EntityPO {

	private QName name;
	
	protected NamedPO(String id) {
		
		super(id);
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
}

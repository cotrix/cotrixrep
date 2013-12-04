package org.cotrix.domain.memory;

import static org.cotrix.common.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.trait.Named;


/**
 * Partial implementation of initialisation parameters for {@link Named} entities.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class NamedMS extends AttributedMS implements Named.State {

	private QName name;
	
	protected NamedMS(String id) {
		
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
	public void name(QName name) {
		
		valid("name",name);
		
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof NamedMS))
			return false;
		NamedMS other = (NamedMS) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}	
	
	
}

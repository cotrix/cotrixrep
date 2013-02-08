package org.cotrix.domain.entities;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Attributed;
import org.cotrix.domain.containers.Bag;
import org.cotrix.domain.traits.Copyable;
import org.cotrix.domain.traits.Named;

/**
 * Partial implementation of {@link Named}, {@link Attributed}, and {@link Copyable} domain objects.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class CoreEntity<T extends CoreEntity<T>> implements Named,Attributed, Copyable<T> {

	private final QName name;
	
	private final Bag<Attribute> attributes;
	
	/**
	 * Creates an instance with a given name and given attributes.
	 * @param name
	 * @param attributes
	 */
	public CoreEntity(QName name, Bag<Attribute> attributes) {
		
		valid(name);
		this.name=name;
		
		notNull("attributes", attributes);
		this.attributes=attributes;
	}
	
	public QName name() {
		return name;
	}
	
	public Bag<Attribute> attributes() {
		return attributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoreEntity<?> other = (CoreEntity<?>) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	
}

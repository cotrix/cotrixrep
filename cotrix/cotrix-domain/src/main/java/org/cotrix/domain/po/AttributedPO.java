package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.ArrayList;
import java.util.Collection;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.trait.Attributed;
/**
 * Partial implementation of initialisation parameters for {@link Attributed} entities.
 * 
 * @author Fabio Simeoni
 *
 */
public abstract class AttributedPO<T extends Attributed.Abstract<T>> extends DomainPO<T> implements Attributed.State<T> {

	private Collection<Attribute.State> attributes = new ArrayList<Attribute.State>();

	protected AttributedPO(String id) {
		super(id);
	}

	@Override
	public Collection<Attribute.State> attributes() {
		return attributes;
	}
	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void attributes(Collection<Attribute.State> attributes) {
		
		notNull("attributes",attributes);
	
		this.attributes = attributes;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("all")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof AttributedPO))
			return false;
		AttributedPO other = (AttributedPO) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	}
		
}

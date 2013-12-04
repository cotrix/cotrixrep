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
public abstract class AttributedPO extends IdentifiedPO implements Attributed.State {

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
		
}

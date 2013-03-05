package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.entity.AttributedEntity;

/**
 * A partial implementation of parameter objects for {@link AttributedEntity}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributedPO extends NamedPO {

	private Bag<Attribute> attributes = new Bag<Attribute>(Collections.<Attribute>emptyList());

	protected AttributedPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the attributes parameter.
	 * @return the parameter
	 */
	public Bag<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(Bag<Attribute> attributes) {
		
		notNull(attributes);
		
		propagateChangeFrom(attributes);
		
		this.attributes = attributes;
	}
}

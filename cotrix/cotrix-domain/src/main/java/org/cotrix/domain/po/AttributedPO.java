package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.primitive.container.MutableContainer;
import org.cotrix.domain.primitive.entity.AttributedEntity;

/**
 * Partial implementation of initialisation parameters for {@link AttributedEntity}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributedPO extends EntityPO {

	private MutableContainer<Attribute> attributes = new MutableContainer<Attribute>(Collections.<Attribute>emptyList());

	protected AttributedPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the attributes parameter.
	 * @return the parameter
	 */
	public MutableContainer<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(MutableContainer<Attribute> attributes) {
		
		notNull(attributes);
		
		propagateChangeFrom(attributes);
		
		this.attributes = attributes;
	}
}

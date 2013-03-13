package org.cotrix.domain.po;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.trait.Attributed;

/**
 * Partial implementation of initialisation parameters for {@link Attributed} entities.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributedPO extends EntityPO {

	private PContainer<Attribute.Private> attributes = new PContainer<Attribute.Private>(Collections.<Attribute.Private>emptyList());

	protected AttributedPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the attributes parameter.
	 * @return the parameter
	 */
	public PContainer<Attribute.Private> attributes() {
		return attributes;
	}

	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(PContainer<Attribute.Private> attributes) {
		
		notNull(attributes);
	
		propagateChangeFrom(attributes);
		
		this.attributes = attributes;
		
	}
	
	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(List<? extends Attribute> attributes) {
		
		notNull(attributes);
		
		//switches from public to private interfaces
		PContainer<Attribute.Private> privateAttributes = new PContainer<Attribute.Private>(reveal(attributes,Attribute.Private.class));
		
		setAttributes(privateAttributes);

	}
}

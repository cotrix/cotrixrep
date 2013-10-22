package org.cotrix.domain.po;

import static org.cotrix.common.Utils.*;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Container;
import org.cotrix.domain.trait.Attributed;
/**
 * Partial implementation of initialisation parameters for {@link Attributed} entities.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributedPO extends DomainPO {

	private Container.Private<Attribute.Private> attributes = new Container.Private<Attribute.Private>(Collections.<Attribute.Private>emptyList());

	protected AttributedPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the attributes parameter.
	 * @return the parameter
	 */
	public Container.Private<Attribute.Private> attributes() {
		return attributes;
	}

	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(Container.Private<Attribute.Private> attributes) {
		
		notNull("attributes",attributes);
	
		this.attributes = attributes;
		
	}
	
	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(List<? extends Attribute> attributes) {
		
		notNull("attributes",attributes);
		
		//switches from public to private interfaces
		Container.Private<Attribute.Private> privateAttributes = new Container.Private<Attribute.Private>(reveal(attributes,Attribute.Private.class));
		
		setAttributes(privateAttributes);

	}
}

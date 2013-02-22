package org.cotrix.domain.pos;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.AttributedObject;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.utils.Utils;

/**
 * A partial implementation of parameter objects for {@link AttributedObject}s.
 * 
 * @author Fabio Simeoni
 *
 */
public class AttributedPO extends ObjectPO {

	private BaseBag<Attribute> attributes = new BaseBag<Attribute>();

	protected AttributedPO(String id) {
		super(id);
	}
	
	/**
	 * Returns the attributes parameter.
	 * @return the parameter
	 */
	public BaseBag<Attribute> attributes() {
		return attributes;
	}

	/**
	 * Sets the attribute parameter.
	 * @param attributes the parameter
	 */
	public void setAttributes(BaseBag<Attribute> attributes) {
		Utils.notNull(attributes);
		this.attributes = attributes;
	}
}

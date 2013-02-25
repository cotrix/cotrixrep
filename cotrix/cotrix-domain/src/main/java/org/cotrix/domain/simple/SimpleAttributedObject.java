package org.cotrix.domain.simple;

import static org.cotrix.domain.utils.Utils.*;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.common.AttributedObject;
import org.cotrix.domain.common.Bag;
import org.cotrix.domain.common.BaseBag;
import org.cotrix.domain.pos.AttributedPO;

/**
 * Partial implementation of {@link AttributedObject}.
 * 
 * @author Fabio Simeoni
 *
 * <T> the type of the entity
 */
//we specialise type parameter because implementing update() requires we have access to attributes of the input
abstract class SimpleAttributedObject<T extends AttributedObject<T>> extends SimpleObject<T> implements AttributedObject<T> {

	
	private final BaseBag<Attribute> attributes;

	/**
	 * Creates an instance with a given name and given attributes.
	 * @param name
	 * @param attributes
	 */
	public SimpleAttributedObject(AttributedPO params) {
		
		super(params);
		
		notNull("attributes", params.attributes());
		this.attributes=params.attributes();
	}
	
	@Override
	public BaseBag<Attribute> attributes() {
		return attributes;
	}
	
	protected void buildPO(AttributedPO po) {
		
		po.setAttributes(attributes);
		
		super.buildPO(po);
	}
	
	@Override
	public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);
		
		Bag<Attribute> attributes = delta.attributes();

		this.attributes().update(attributes);
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
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
		SimpleAttributedObject<?> other = (SimpleAttributedObject<?>) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	}

	
}

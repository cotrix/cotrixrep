package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.po.AttributedPO;
import org.cotrix.domain.primitive.container.Bag;
import org.cotrix.domain.primitive.container.Container;
import org.cotrix.domain.primitive.entity.AttributedEntity;
import org.cotrix.domain.utils.IdGenerator;

/**
 * Partial implementation of {@link AttributedEntity}.
 * 
 * @author Fabio Simeoni
 *
 * <T> the type of the entity
 */
//we specialise type parameter because implementing update() requires we have access to attributes of the input
public abstract class SimpleAttributedEntity<T extends AttributedEntity<T>> extends SimpleNamedEntity<T> implements AttributedEntity<T> {

	
	private final Bag<Attribute> attributes;

	/**
	 * Creates an instance with a given name and given attributes.
	 * @param name
	 * @param attributes
	 */
	public SimpleAttributedEntity(AttributedPO params) {
		
		super(params);
		
		this.attributes=params.attributes();
	}
	
	@Override
	public Bag<Attribute> attributes() {
		return attributes;
	}
	
	protected void fillPO(IdGenerator generator,AttributedPO po) {
		
		po.setAttributes(attributes.copy(generator));
		
		super.fillPO(po);
	}
	
	@Override
	public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);
		
		Container<Attribute> attributes = delta.attributes();

		this.attributes.update(attributes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleAttributedEntity<?> other = (SimpleAttributedEntity<?>) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	};



	
}

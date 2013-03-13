package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.po.AttributedPO;
import org.cotrix.domain.primitive.PContainer;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Attributed;

/**
 * Partial implementation of {@link PAttributed} objects.
 * 
 * @author Fabio Simeoni
 *
 * <T> the type of the object
 */
//we specialise type parameter because implementing update() requires we have access to attributes of the input
public abstract class SimpleAttributed<T extends Attributed.Private<T>> extends SimpleBase<T> implements Attributed.Private<T> {

	
	private final PContainer<Attribute.Private> attributes;

	/**
	 * Creates an instance with a given name and given attributes.
	 * @param name
	 * @param attributes
	 */
	public SimpleAttributed(AttributedPO params) {
		
		super(params);
		
		this.attributes=params.attributes();
	}
	
	@Override
	public PContainer<Attribute.Private> attributes() {
		return attributes;
	}
	
	protected void fillPO(IdGenerator generator,AttributedPO po) {
	
		po.setAttributes(attributes.copy(generator));
	}
	
	@Override
	public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);
		
		
		PContainer<Attribute.Private> attributes = delta.attributes();

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
		SimpleAttributed<?> other = (SimpleAttributed<?>) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	};



	
}

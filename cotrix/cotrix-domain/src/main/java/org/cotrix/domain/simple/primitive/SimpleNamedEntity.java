package org.cotrix.domain.simple.primitive;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.NamedPO;
import org.cotrix.domain.primitive.entity.NamedEntity;
import org.cotrix.domain.spi.IdGenerator;

/**
 * Partial {@link NamedEntity} implementation.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public abstract class SimpleNamedEntity<T extends NamedEntity<T>> extends SimpleAttributedEntity<T> implements NamedEntity<T> {

	private QName name;
	
	protected SimpleNamedEntity(NamedPO po) {

		super(po);
		this.name=po.name();
	}
	
	protected void fillPO(IdGenerator generator,NamedPO po) {
		super.fillPO(generator, po);
		po.setName(name());
	}
	
	@Override
	public QName name() {
		return name;
	}
	
	@Override
	public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
		
		super.update(delta);
		
		//name has changed?
		if (!delta.name().equals(name()))
			this.name=delta.name();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SimpleNamedEntity<?> other = (SimpleNamedEntity<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
		
	
}

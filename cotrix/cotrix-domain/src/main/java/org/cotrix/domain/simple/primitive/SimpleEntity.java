package org.cotrix.domain.simple.primitive;

import org.cotrix.domain.po.EntityPO;
import org.cotrix.domain.primitive.entity.Entity;
import org.cotrix.domain.primitive.entity.NamedEntity;
import org.cotrix.domain.trait.Change;

/**
 * Partial {@link NamedEntity} implementation.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
public abstract class SimpleEntity<T extends Entity<T>> implements Entity<T> {

	private final String id; //TODO: how do I relate to different identification strategy in access layer?
	private Change change;
	
	protected SimpleEntity(EntityPO po) {

		this.id=po.id();
		this.change=po.change();
	}
	
	@Override
	public String id() {
		return id;
	}
	
	@Override
	public boolean isDelta() {
		return change!=null;
	}
	
	@Override
	public Change change() {
		return change;
	}
	
	@Override
	public void reset() {
		this.change=null;	
	}
	
	@Override
	public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
		
		if (id()==null)
			throw new IllegalArgumentException(this+" as no identifier, hence it cannot be updated");
		
		isValid(delta);
	}
	
	//helper
	private void isValid(T delta) {
		
		Change status = delta.change();
		
		//is the input a delta object ?
		if (status==null)
			throw new IllegalArgumentException("object "+id+" is not a delta update");
		
		//and is it a delta for this object?
		if (id()!=null && ! id().equals(delta.id()))
			throw new IllegalArgumentException("object "+delta.id()+" is not a delta update of this object ("+id()+")");
		
		//is it a change (removal and addition must be catered for at container level
		if (status!=Change.MODIFIED)
			throw new IllegalArgumentException("object "+id+" with update status "+status+" does not capture a change to this object ("+id()+")");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((change == null) ? 0 : change.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SimpleEntity<?> other = (SimpleEntity<?>) obj;
		if (change != other.change)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	

	
	
}

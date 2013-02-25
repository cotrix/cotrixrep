package org.cotrix.domain.simple;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.DomainObject;
import org.cotrix.domain.pos.ObjectPO;
import org.cotrix.domain.traits.Change;

/**
 * Partial {@link DomainObject} implementation.
 * 
 * @author Fabio Simeoni
 *
 * @param <T> the type of the entity
 */
abstract class SimpleObject<T extends DomainObject<T>> implements DomainObject<T> {

	private final String id; //TODO: how do I relate to different identification strategy in access layer?
	private QName name;
	private Change change;
	
	protected SimpleObject(ObjectPO po) {

		this.id=po.id();
		this.name=po.name();
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
	
	//as java has no covariance, subclasses effectively overload this method, recursively
	protected void buildPO(ObjectPO po) {
		po.setName(name());
	}
	
	@Override
	public QName name() {
		return name;
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
			throw new IllegalArgumentException("cannot be updated uncommitted "+this);
		
		isValid(delta);
		
		//name has changed?
		if (!delta.name().equals(name()))
			this.name=delta.name();
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		SimpleObject<?> other = (SimpleObject<?>) obj;
		if (change != other.change)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	

	
	
}

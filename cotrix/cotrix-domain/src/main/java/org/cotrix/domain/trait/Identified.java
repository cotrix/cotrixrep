package org.cotrix.domain.trait;

import org.cotrix.domain.po.EntityPO;


/**
 * A domain object with an identity.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Identified {

	/**
	 * Returns the identifier of this object.
	 * @return the identifier
	 */
	String id();

	
	/**
	 * {@link Mutable} and {@link Copyable} implementation of {@link Identified}.
	 * 
	 * @param <T> the concrete type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> implements Identified,Mutable<T>,Copyable<T> {
		
		private String id;
		private Change change;
		
		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		protected Abstract(EntityPO po) {

			this.id=po.id();
			this.change=po.change();
		}
		
		@Override
		public String id() {
			return id;
		}
		
		public void setId(String id) {
		
			if (id()!=null)
				throw new IllegalStateException("object has already an identifier ("+id()+")");
			
			this.id=id;
		}
		
		public boolean isChangeset() {
			return change!=null;
		}
		
		public Change change() {
			return change;
		}
		
		public void reset() {
			this.change=null;	
		}
		
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
			Abstract<?> other = (Abstract<?>) obj;
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
}

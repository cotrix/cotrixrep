package org.cotrix.domain.trait;

import static org.cotrix.common.Utils.*;

import org.cotrix.domain.po.DomainPO;

/**
 * The base interface of all domain objects.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Identified {

	/**
	 * Returns the identifier of this object.
	 * 
	 * @return the identifier
	 */
	String id();

	
	/**
	 * Default {@link Identified} implementation.
	 * 
	 * @param <T> the self type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> implements Identified {

		private String id;
		private Status change;

		protected Abstract(DomainPO po) {

			this.id = po.id();
			this.change = po.change();
		}

		@Override
		public String id() {
			return id;
		}

		/**
		 * Sets the identifier of this object.
		 * @param id the identifier
		 * 
		 * @throws IllegalArgumentException if the identifier is <code>null</code>
 		 * @throws IllegalStateException if this object is already identified
		 */
		public void setId(String id) throws IllegalStateException {

			valid("object identifier",id);
			
			if (id() != null)
				throw new IllegalStateException("object has already an identifier (" + id() + ")");

			this.id = id;
		}

		 /** Returns <code>true</code> if this object is a changeset.
		 * @return <code>true</code> if this object is a changeset
		 */
		public boolean isChangeset() {
			return change != null;
		}

		
		/**
		 * Returns the type of change represented by this object, if any.
		 * @return the type of change
		 */
		public Status status() {
			return change;
		}

		/**
		 * Applies a changeset to this object.
		 * 
		 * @param changeset the changeset
		 * @throws IllegalArgumentException if the changeset has a status other than {@link Status#MODIFIED}
		 * @throws IllegalStateException if this object is unidentified or the changeset does not match its identifier
		 */
		public void update(T changeset) throws IllegalArgumentException, IllegalStateException {

			if (this.id == null)
				throw new IllegalStateException(this + " has no identifier and cannot be updated");

			if (changeset.status() == null || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("object " + id + " cannot be updated with a "
						+ (changeset.status() == null ? "NEW" : changeset.status()) + " object");

			if (!id().equals(changeset.id()))
				throw new IllegalStateException("object " + changeset.id()
						+ " is not a changeset for object " + id());

		}
		
		
		/**
		 * Returns an exact copy of this object.
		 * 
		 * @return an exact copy of this object
		 */
		public final T copy() {
			return copy(true);
		}
		
		
		//used for copying (withId=true) and versioning (withId=false)
		public abstract T copy(boolean withId);
		
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

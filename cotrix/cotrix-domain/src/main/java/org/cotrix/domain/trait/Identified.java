package org.cotrix.domain.trait;

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
	 * {@link Mutable} and {@link Copyable} implementation of {@link Identified}.
	 * 
	 * @param <T> the concrete type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> implements Identified {

		private String id;
		private Status change;

		/**
		 * Creates a new instance from a given set of parameters.
		 * 
		 * @param params the parameters
		 */
		protected Abstract(DomainPO po) {

			this.id = po.id();
			this.change = po.change();
		}

		@Override
		public String id() {
			return id;
		}

		public void setId(String id) {

			if (id() != null)
				throw new IllegalStateException("object has already an identifier (" + id() + ")");

			this.id = id;
		}

		 /** Returns <code>true</code> if the object represents a change.
		 * @return <code>true</code> if the object represents a change
		 */
		public boolean isChangeset() {
			return change != null;
		}

		
		/**
		 * Returns the type of incremental change represented by this object, if any.
		 * @return the type of change
		 */
		public Status status() {
			return change;
		}

		public void reset() {
			this.change = null;
		}

		/**
		 * Updates this object with a given delta object.
		 * 
		 * @param delta the delta object
		 * @throws IllegalArgumentException if the delta object is malformed
		 * @throws IllegalStateException if the object cannot be updated with the delta object
		 */
		public void update(T changeset) throws IllegalArgumentException, IllegalStateException {

			// note: this object may have an identifier without having been persisted. we will need to detect the
			// problem later on, e.g. when the object is updated in the repository
			if (this.id == null)
				throw new IllegalArgumentException(this + " has not been persisted yet, hence cannot be updated");

			
			// is the input a delta object ?
			if (changeset.status() == null || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("object " + id + " cannot be updated with a "
						+ (changeset.status() == null ? "NEW" : changeset.status()) + " object");

			// and is it a delta for this object?
			if (!id().equals(changeset.id()))
				throw new IllegalArgumentException("object " + changeset.id()
						+ " is not a changeset for object " + id());

		}
		
		/**
		 * Returns a copy of this object.
		 * 
		 * @return the copy
		 */
		public final T copy() {
			return copy(true);
		}
		
		public abstract T copy(boolean retainId);

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

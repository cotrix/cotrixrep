package org.cotrix.domain.trait;

import static org.cotrix.common.Utils.*;

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

	
	static interface State<T extends Identified.Abstract<T>> {
		
		Status status();

		String id();
		
		void id(String id);
	}
	
	/**
	 * Default {@link Identified} implementation.
	 * 
	 * @param <T> the self type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> implements Identified {

		@Override
		public String id() {
			return state().id();
		}

		/**
		 * Sets the identifier of this object.
		 * @param id the identifier
		 * 
		 * @throws IllegalArgumentException if the identifier is <code>null</code>
 		 * @throws IllegalStateException if this object is already identified
		 */
		public void id(String id) throws IllegalStateException {

			valid("object identifier",id);
			
			if (state().id() != null)
				throw new IllegalStateException(this.getClass().getCanonicalName()+" has already an identifier (" + state().id() + ")");

			state().id(id);
		}

		 /** Returns <code>true</code> if this object is a changeset.
		 * @return <code>true</code> if this object is a changeset
		 */
		public boolean isChangeset() {
			return state().status() != null;
		}

		
		/**
		 * Returns the type of change represented by this object, if any.
		 * @return the type of change
		 */
		public Status status() {
			return state().status();
		}

		/**
		 * Applies a changeset to this object.
		 * 
		 * @param changeset the changeset
		 * @throws IllegalArgumentException if the changeset has a status other than {@link Status#MODIFIED}
		 * @throws IllegalStateException if this object is unidentified or the changeset does not match its identifier
		 */
		public void update(T changeset) throws IllegalArgumentException, IllegalStateException {

			if (state().id() == null)
				throw new IllegalStateException(this + " has no identifier and cannot be updated");

			if (changeset.status() == null || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("object " + state().id() + " cannot be updated with a "
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

		public abstract State<T> state();

		
		
		
	}
}

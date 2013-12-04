package org.cotrix.domain.trait;

import static org.cotrix.common.Utils.*;

/**
 * The read-only interface common to all domain entities.
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

	static interface State {

		String id();

		void id(String id);

		Status status();

		void status(Status status);

	}

	/**
	 * @param <SELF> the type of implementations
	 */
	public abstract class Abstract<SELF extends Abstract<SELF, S>, S extends State> implements Identified {

		// NOTE: we need SELF because we have a covariant method #update(SELF)
		// NOTE: S is to give state back to family classes, which complicates client-use
		// we could use a template method but it's less safe and more redundant

		private S state;

		public Abstract(S state) {

			notNull("state bean", state);

			this.state = state;
		}

		public S state() {
			return state;
		}

		@Override
		public String id() {
			return state.id();
		}

		public void id(String id) throws IllegalStateException {

			valid("object identifier", id);

			if (state.id() != null)
				throw new IllegalStateException(this.getClass().getCanonicalName() + " has already an identifier ("
						+ state.id() + ")");

			state.id(id);
		}

		public boolean isChangeset() {
			return state.status() != null;
		}

		public Status status() {
			return state.status();
		}

		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			notNull(this.getClass().getCanonicalName() + "'s changeset", changeset);

			if (state.id() == null)
				throw new IllegalStateException(this + " has no identifier and cannot be updated");

			if (changeset.status() == null || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("object " + state.id() + " cannot be updated with a "
						+ (changeset.status() == null ? "NEW" : changeset.status()) + " object");

			if (!id().equals(changeset.id()))
				throw new IllegalStateException("object " + changeset.id() + " is not a changeset for object " + id());

		}

		/**
		 * Returns an exact copy of this object.
		 * 
		 * @return an exact copy of this object
		 */
		public final SELF copy() {
			return copy(true);
		}

		// used for copying (withId=true) and versioning (withId=false)
		public abstract SELF copy(boolean withId);

		
		
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id() == null) ? 0 : id().hashCode());
			result = prime * result + ((status() == null) ? 0 : status().hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Identified.Abstract other = (Identified.Abstract) obj;
			if (id() == null) {
				if (other.id() != null)
					return false;
			} else if (!id().equals(other.id()))
				return false;
			if (status() != other.status())
				return false;
			return true;
		}

	}
}

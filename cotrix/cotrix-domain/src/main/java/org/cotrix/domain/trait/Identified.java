package org.cotrix.domain.trait;

import static org.cotrix.common.Utils.*;

/**
 * The read-only interface common to all domain entities.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Identified {

	
	//public read-only interface
	
	/**
	 * Returns the identifier of this object.
	 * 
	 * @return the identifier
	 */
	String id();


	//private state interface
	
	interface State {
	
		String id();
		
		Status status();

	}
	
	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>, S extends State> implements Identified {

		//NOTE: we need SELF because we have a covariant method #update(SELF)
		//NOTE:	we need S to give state back to subclasses
		
		private S state;
		
		
		public Abstract(S state) {
			
			notNull("state",state);
			
			this.state=state;
		}
		
		public S state() {
			return state;
		}

		
		@Override
		public String id() {
			return state.id();
		}

		public boolean isChangeset() {
			return status() != null;
		}

		public Status status() {
			return state.status();
		}


		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			notNull(this.getClass().getCanonicalName()+"'s changeset",changeset);
			
			if (isChangeset())
				throw new IllegalStateException("entity " + state.id() + "("+getClass().getCanonicalName()+") is a changeset and cannot be updated");

			if (changeset.status() == null || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("entity " + state.id() + "("+getClass().getCanonicalName()+") cannot be updated with a "
						+ (changeset.status() == null ? "NEW" : changeset.status()) + " object");

			if (!id().equals(changeset.id()))
				throw new IllegalArgumentException("changeset " + changeset.id()
						+ "("+changeset.getClass().getCanonicalName()+") is not a changeset for entity " + id());

		}
		
		//delegates to state
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((state == null) ? 0 : state.hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Abstract))
				return false;
			Abstract other = (Abstract) obj;
			if (state == null) {
				if (other.state != null)
					return false;
			} else if (!state.equals(other.state))
				return false;
			return true;
		}
		
	}
}

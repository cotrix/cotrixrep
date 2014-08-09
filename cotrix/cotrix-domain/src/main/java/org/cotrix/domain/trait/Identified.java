package org.cotrix.domain.trait;

import static org.cotrix.common.CommonUtils.*;

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
	
	interface Bean {
	
		String id();
		
		Status status();

	}
	
	//private logic
	
	abstract class Private<SELF extends Private<SELF,B>, B extends Bean> implements Identified {

		//NOTE: we need SELF because we have a covariant method #update(SELF)
		//NOTE:	we need S to give state back to subclasses
		
		private B bean;
		
		
		public Private(B bean) {
			
			notNull("bean",bean);
			
			this.bean=bean;
		}
		
		public B bean() {
			return bean;
		}

		
		@Override
		public String id() {
			return bean.id();
		}

		public boolean isChangeset() {
			return status() == Status.MODIFIED || status() == Status.DELETED;
		}

		public Status status() {
			return bean.status();
		}


		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			notNull(this.getClass().getCanonicalName()+"'s changeset",changeset);
			
			if (isChangeset())
				throw new IllegalStateException("entity " + bean.id() + "("+getClass().getCanonicalName()+") is a changeset and cannot be updated");

			if (!changeset.isChangeset() || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("entity " + bean.id() + "("+getClass().getCanonicalName()+") cannot be updated with a "
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
			result = prime * result + ((bean == null) ? 0 : bean.hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Private))
				return false;
			Private other = (Private) obj;
			if (bean == null) {
				if (other.bean != null)
					return false;
			} else if (!bean.equals(other.bean))
				return false;
			return true;
		}
		
	}
}

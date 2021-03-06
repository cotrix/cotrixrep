package org.cotrix.domain.trait;

import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.common.Status.*;

import org.cotrix.domain.common.Status;

public interface Identified {

	
	String id();
	

	//----------------------------------------
	
	interface Bean extends Identified {
	
		Status status();

	}
	
	//----------------------------------------
	
	abstract class Private<SELF extends Private<SELF,B>, B extends Bean> implements Identified {

		//we need (to emulate) SELF because we have a covariant method: #update(SELF)
		//we need B to give beans back to subclasses: #bean()
		
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
			
			return status() == MODIFIED || status() == DELETED;
		}

		
		public Status status() {
			return bean.status();
		}


		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			notNull("changeset",changeset);
			
			if (isChangeset())
				throw new IllegalStateException("entity " + bean.id() + "("+getClass().getCanonicalName()+") is a changeset and cannot be updated");

			if (!changeset.isChangeset() || changeset.status() != Status.MODIFIED)
				throw new IllegalArgumentException("entity " + bean.id() + "("+getClass().getCanonicalName()+") cannot be updated with a "
						+ (changeset.status() == null ? "NEW" : changeset.status()) + " object");

			if (!id().equals(changeset.id()))
				throw new IllegalArgumentException("changeset " + changeset.id()
						+ "("+changeset.getClass().getCanonicalName()+") is not a changeset for entity " + id());

		}
		
		//delegate to bean
		
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

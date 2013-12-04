package org.cotrix.domain.trait;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.NamedPO;

/**
 * A named domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Named {

	/**
	 * Returns the name of this object.
	 * @return the name
	 */
	QName name();
	
	
	
	
	static interface State extends Named, Attributed.State {
		
		void name(QName name);
	}

	
	
	
	/**
	 * Default {@link Named} implementation.
	 * 
	 * @param <T> the self type of instances
	 */
	public abstract class Abstract<T extends Abstract<T,S>,S extends State> extends Attributed.Abstract<T,S> implements Named {
		
		
		public Abstract(S state) {
			super(state);
		}
		
		//invoked by subclasses under copying
		protected void fillPO(boolean withId,NamedPO po) {
			super.fillPO(withId,po);
			po.name(name());
		}
		
		@Override
		public QName name() {
			return state().name();
		}
		
		@Override
		public void update(T changeset) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(changeset);
			
			if (changeset.name()!=null)
				if (changeset.name()==NULL_QNAME)
					throw new IllegalArgumentException("code name "+state().name()+" cannot be erased");
				else
					state().name(changeset.name());
		}
	
		
		
		
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((name() == null) ? 0 : name().hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof Named.Abstract))
				return false;
			Named.Abstract other = (Named.Abstract) obj;
			if (name() == null) {
				if (other.name() != null)
					return false;
			} else if (!name().equals(other.name()))
				return false;
			return true;
		}
	}
}

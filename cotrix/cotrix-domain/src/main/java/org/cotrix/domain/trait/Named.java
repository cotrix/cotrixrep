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
	
	
	
	
	static interface State<T extends Named.Abstract<T>> extends Named, Attributed.State<T> {
		
		void name(QName name);
	}

	
	
	
	/**
	 * Default {@link Named} implementation.
	 * 
	 * @param <T> the self type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> extends Attributed.Abstract<T> implements Named {
		
		
		public Abstract(Named.State<T> state) {
			super(state);
		}
		
		//invoked by subclasses under copying
		protected void fillPO(boolean withId,NamedPO<T> po) {
			super.fillPO(withId,po);
			po.name(name());
		}
		
		@Override
		public abstract Named.State<T> state();
		
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
		
	}
}

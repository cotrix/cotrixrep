package org.cotrix.domain.trait;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

/**
 * A named domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Named {

	
	//public read-only interface
	
	/**
	 * Returns the name of this object.
	 * @return the name
	 */
	QName qname();
	
	
	
	
	interface State extends Identified.State {
		
		QName name();
		
		void name(QName name);
	}

	
	
	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends State & Attributed.State> extends Attributed.Abstract<SELF,S> implements Named {
		
		
		public Abstract(S state) {
			super(state);
		}
		
		@Override
		public QName qname() {
			return state().name();
		}

		
		@Override
		public void update(SELF changeset) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(changeset);
			
			
			if (changeset.qname()!=null)
				if (changeset.qname()==NULL_QNAME)
					throw new IllegalArgumentException("code name "+state().name()+" cannot be erased");
				else 
					state().name(changeset.qname());
		}
		
	}
}

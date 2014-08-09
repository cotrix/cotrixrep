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
	
	
	
	
	interface Bean extends Identified.Bean {
		
		QName qname();
		
		void qname(QName name);
	}

	
	
	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends Bean & Attributed.Bean> extends Attributed.Abstract<SELF,S> implements Named {
		
		
		public Abstract(S state) {
			super(state);
		}
		
		@Override
		public QName qname() {
			return bean().qname();
		}

		
		@Override
		public void update(SELF changeset) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(changeset);
			
			
			if (changeset.qname()!=null)
				if (changeset.qname()==NULL_QNAME)
					throw new IllegalArgumentException("code name "+bean().qname()+" cannot be erased");
				else 
					bean().qname(changeset.qname());
		}
		
	}
}

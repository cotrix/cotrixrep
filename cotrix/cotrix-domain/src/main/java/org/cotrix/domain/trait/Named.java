package org.cotrix.domain.trait;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

public interface Named {

	
	QName qname();
	
	
	
	//----------------------------------------
	
	interface Bean extends Identified.Bean, Named {
		
		void qname(QName name);
	}
	
	

	//----------------------------------------
	
	abstract class Private<SELF extends Private<SELF,B>, B extends Bean> 
	
						 extends Identified.Private<SELF,B> implements Named {
		
		
		public Private(B bean) {		
			super(bean);
		}
		
		
		@Override
		public QName qname() {
			return bean().qname();
		}

		
		@Override
		public void update(SELF changeset) {
			
			super.update(changeset);
			
			if (changeset.qname()!=null)
				
				if (changeset.qname()==NULL_QNAME)
					throw new IllegalArgumentException("code name "+bean().qname()+" cannot be erased");
				else 
					bean().qname(changeset.qname());
		}
		
	}
}

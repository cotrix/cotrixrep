package org.cotrix.domain.trait;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.Containers.Attributes;


public interface Described {

	
	Attributes attributes();
		
	//----------------------------------------
	
	
	interface Bean extends Named.Bean {
		
		Container.Bean<Attribute.Bean> attributes();
		
	}
	

	//----------------------------------------
	
	
	abstract class Private<SELF extends Private<SELF,B>,B extends Bean> 
							
							extends Named.Private<SELF,B> 
							
							implements Described {

		
		public Private(B bean) {
			super(bean);
		}
		

		@Override
		public Attributes attributes() {
			
			return new Attributes(bean().attributes()); //lazy wrapping
		
		}
	

		@Override
		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);
			
			attributes().update(changeset.attributes());
			
		}
		
	}
	
	
}

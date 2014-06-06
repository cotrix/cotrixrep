package org.cotrix.domain.trait;

import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;

/**
 * A domain object with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attributed {

	
	//public read-only interface
	
	/**
	 * Returns the attributes of this object.
	 * 
	 * @return the attributes
	 */
	NamedContainer<? extends Attribute> attributes();
	
	
	
	//private state interface
	
	interface State {
		
		NamedStateContainer<Attribute.State> attributes();
		
	}
	

	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends State & Identified.State> extends Identified.Abstract<SELF,S> implements Attributed {


		public Abstract(S state) {
			
			super(state);

		}
		

		@Override
		public NamedContainer.Private<Attribute.Private,Attribute.State> attributes() {
			
			return namedContainer(state().attributes());
		
		}

		@Override
		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			attributes().update(changeset.attributes());
			
			NamedStateContainer<Attribute.State> attributes = state().attributes();
			
			if (attributes.contains(UPDATE_TIME))
		
				attributes.lookup(UPDATE_TIME).value(time());
			
			else 
				
				attributes.add(timestamp(UPDATE_TIME));
		

			if (attributes.contains(UPDATED_BY))
				
				updatedBy(attributes.lookup(UPDATED_BY));
			
			else 
				
				attributes.add(updatedBy());
			
		}

	}
}

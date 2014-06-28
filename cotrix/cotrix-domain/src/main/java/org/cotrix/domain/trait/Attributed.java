package org.cotrix.domain.trait;

import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Utils.*;

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
			
			//these may be materialised from storage, sacrifice legibility for minimal handling
			NamedStateContainer<Attribute.State> attributes = state().attributes();
			
			if (timestampIn(attributes))
		
				updateTimestampAndUserIn(attributes);
				
			else
				
				addTimestampAndUserTo(attributes);
		

		}

		//helpers
		private boolean timestampIn(NamedStateContainer<Attribute.State> attributes) {
			return attributes.contains(UPDATE_TIME.qname());
		}
		
		private void updateTimestampAndUserIn(NamedStateContainer<Attribute.State> attributes) {
			attributes.lookup(UPDATE_TIME.qname()).value(time());
			attributes.lookup(UPDATED_BY.qname()).value(currentUser());
		}
		
		private void addTimestampAndUserTo(NamedStateContainer<Attribute.State> attributes) {
			attributes.add(stateof(attribute().with(UPDATE_TIME).value(time())));
			attributes.add(stateof(attribute().with(UPDATED_BY).value(currentUser())));
		}
		
	}
	
	
}

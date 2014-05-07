package org.cotrix.domain.trait;

import static java.text.DateFormat.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Calendar;

import org.cotrix.domain.common.Attribute;
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
			
			Attribute.State updateTime = null;
			
			for (Attribute.State a : state().attributes())
				if (a.name().equals(UPDATE_TIME))
					updateTime = a;
	
			if (updateTime==null) 
				state().attributes().add(timestamp(UPDATE_TIME));
			else 
				updateTime.value(getDateTimeInstance().format(Calendar.getInstance().getTime()));
			
			
		}

	}
}

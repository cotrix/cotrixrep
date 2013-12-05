package org.cotrix.domain.trait;

import static java.text.DateFormat.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.memory.AttributeMS;

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
	Container<? extends Attribute> attributes();
	
	
	//private state interface
	
	interface State extends Identified.State {
		
		Collection<Attribute.State> attributes();
		
		void attributes(Collection<Attribute.State> attributes);
		
	}
	

	//private logic
	
	abstract class Abstract<SELF extends Abstract<SELF,S>,S extends State> extends Identified.Abstract<SELF,S> implements Attributed {


		public Abstract(S state) {
			
			super(state);
			
			Attribute.State created = timestamp(CREATION_TIME);
			if (!isChangeset() && !state.attributes().contains(created))
				state.attributes().add(created);

		}
		

		@Override
		public Container.Private<Attribute.Private,Attribute.State> attributes() {
			
			return container(state().attributes());
		
		}
		
		public void build(S state) {
			state.attributes(attributes().copy());
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

		// helpers
		private Attribute.State timestamp(QName name) {

			AttributeMS state = new AttributeMS();
			state.name(name);
			String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
			state.value(value);
			state.type(SYSTEM_TYPE);
			return state;

		}
	}
}

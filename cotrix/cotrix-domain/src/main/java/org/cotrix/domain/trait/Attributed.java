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
import org.cotrix.domain.memory.AttributedMS;

/**
 * A domain object with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public interface Attributed {

	/**
	 * Returns the attributes of this object.
	 * 
	 * @return the attributes
	 */
	Container<? extends Attribute> attributes();
	
	
	static interface State extends Identified.State {
		
		Collection<Attribute.State> attributes();
		
	}

	/**
	 * An {@link Identified.Abstract} implementation of {@link Attributed}.
	 * 
	 * @param <T> the concrete type of instances
	 */
	abstract class Abstract<T extends Abstract<T,S>,S extends State> extends Identified.Abstract<T,S> implements Attributed {

		/**
		 * Creates a new instance from a given set of parameters.
		 * 
		 * @param state the parameters
		 */
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
		
		protected void buildState(boolean withId, AttributedMS state) {
			state.attributes(attributes().copy(withId).state());
		}

		@Override
		public void update(T changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			Container.Private<Attribute.Private,Attribute.State> attributes = changeset.attributes();

			attributes().update(attributes);
			
			Attribute.State updateTime = null;
			
			for (Attribute.State a : attributes().state()) {
				if (a.name().equals(UPDATE_TIME)) {
					updateTime = a;
				}
			}
			if (updateTime==null) 
				attributes().state().add(timestamp(UPDATE_TIME));
			else {
				String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
				updateTime.value(value);
			}
			
		}

		// helpers
		private Attribute.State timestamp(QName name) {

			AttributeMS po = new AttributeMS(null);
			po.name(name);
			String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
			po.value(value);
			po.type(SYSTEM_TYPE);
			return po;

		}
	}
}

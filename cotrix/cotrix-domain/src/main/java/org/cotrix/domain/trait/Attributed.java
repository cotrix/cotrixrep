package org.cotrix.domain.trait;

import static java.text.DateFormat.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Calendar;
import java.util.Collection;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.po.AttributedPO;

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
			if (!isChangeset()) {
				for (Attribute a : state.attributes())
					if (a.name().equals(CREATION_TIME))
						return;
				state.attributes().add(created);
			}
		}
		

		@Override
		public Container.Private<Attribute.Private,Attribute.State> attributes() {
			
			return container(state().attributes());

		}
		
		

		protected void fillPO(boolean withId, AttributedPO po) {
			po.attributes(attributes().copy(withId).state());
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

			AttributePO po = new AttributePO(null);
			po.name(name);
			String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
			po.value(value);
			po.type(SYSTEM_TYPE);
			return po;

		}
		
		
		
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((attributes() == null) ? 0 : attributes().hashCode());
			return result;
		}

		@Override
		@SuppressWarnings("all")
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (!(obj instanceof Attributed.Abstract))
				return false;
			Attributed.Abstract other = (Attributed.Abstract) obj;
			if (attributes() == null) {
				if (other.attributes() != null)
					return false;
			} else if (!attributes().equals(other.attributes()))
				return false;
			return true;
		}
	}
}

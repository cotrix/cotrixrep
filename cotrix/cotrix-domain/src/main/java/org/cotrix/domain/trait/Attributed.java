package org.cotrix.domain.trait;

import static java.text.DateFormat.*;
import static org.cotrix.domain.utils.Constants.*;

import java.util.Calendar;

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

	/**
	 * An {@link Identified.Abstract} implementation of {@link Attributed}.
	 * 
	 * @param <T> the concrete type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> extends Identified.Abstract<T> implements Attributed {

		private Container.Private<Attribute.Private> attributes;

		// //////////////////////////////////////////////////////////////////////////////

		/**
		 * Creates a new instance from a given set of parameters.
		 * 
		 * @param params the parameters
		 */
		public Abstract(AttributedPO params) {

			super(params);

			this.attributes = params.attributes();
			
			if (!isChangeset())
				attributes.objects().add(timestamp(CREATION_TIME));

		}

		@Override
		public Container.Private<Attribute.Private> attributes() {
			return attributes;
		}

		protected void fillPO(boolean withId, AttributedPO po) {

			po.setAttributes(attributes.copy(withId));
		}

		@Override
		public void update(T changeset) throws IllegalArgumentException, IllegalStateException {

			super.update(changeset);

			Container.Private<Attribute.Private> attributes = changeset.attributes();

			this.attributes.update(attributes);
			
			Attribute.Private updateTime = null;
			
			for (Attribute.Private a : this.attributes.objects()) {
				if (a.name().equals(UPDATE_TIME)) {
					updateTime = a;
				}
			}
			if (updateTime==null) 
				this.attributes.objects().add(timestamp(UPDATE_TIME));
			else {
				String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
				updateTime.value(value);
			}
			
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Attributed.Abstract<?> other = (Attributed.Abstract<?>) obj;
			if (attributes == null) {
				if (other.attributes != null)
					return false;
			} else if (!attributes.equals(other.attributes))
				return false;
			return true;
		};
		
		// helpers
		private Attribute.Private timestamp(QName name) {

			AttributePO po = new AttributePO(null);
			po.setName(name);
			String value = getDateTimeInstance().format(Calendar.getInstance().getTime());
			po.setValue(value);
			po.setType(SYSTEM_TYPE);
			return new Attribute.Private(po);

		}
	}
}

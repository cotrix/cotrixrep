package org.cotrix.domain.trait;

import java.util.List;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.Container;
import org.cotrix.domain.po.AttributedPO;
import org.cotrix.domain.spi.IdGenerator;

/**
 * A domain object with {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Attributed {

	/**
	 * Returns the attributes of this object.
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
		
		//concessions to ORM that knows not how to map Container<T> even if we use it here with a concrete type...
		//this also forces us to make field attributes non-final....
		@SuppressWarnings("all")
		private void setORMAttributes(List<Attribute.Private> attributes) {
			if (attributes!=null) //no idea why ORM arrives with NULL before it arrives with not-NULL value
				this.attributes = new Container.Private<Attribute.Private>(attributes);
		}
		
		@SuppressWarnings("all")
		private List<Attribute.Private> getORMAttributes() {
			return attributes.objects();
		}
		
		////////////////////////////////////////////////////////////////////////////////

		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		public Abstract(AttributedPO params) {
			
			super(params);
			
			this.attributes=params.attributes();
		}
		
		@Override
		public Container.Private<Attribute.Private> attributes() {
			return attributes;
		}
			
		protected void fillPO(IdGenerator generator,AttributedPO po) {
		
			po.setAttributes(attributes.copy(generator));
		}
		
		@Override
		public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(delta);
			
			
			Container.Private<Attribute.Private> attributes = delta.attributes();

			this.attributes.update(attributes);
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

		
	}
}

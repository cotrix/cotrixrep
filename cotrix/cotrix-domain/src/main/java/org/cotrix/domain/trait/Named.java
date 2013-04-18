package org.cotrix.domain.trait;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.NamedPO;
import org.cotrix.domain.spi.IdGenerator;

/**
 * A named domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Named {

	/**
	 * Returns the name of this object.
	 * @return the name
	 */
	QName name();

	
	/**
	 * An {@link Attributed.Abstract} implementation of {@link Named}.
	 * 
	 * @param <T> the concrete type of instances
	 */
	public abstract class Abstract<T extends Abstract<T>> extends Attributed.Abstract<T> implements Named {
		
		
		private QName name;
		
		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		protected Abstract(NamedPO params) {

			super(params);
			this.name=params.name();
		}
		
		protected void fillPO(IdGenerator generator,NamedPO po) {
			super.fillPO(generator, po);
			po.setName(name());
		}
		
		@Override
		public QName name() {
			return name;
		}
		
		@Override
		public void update(T delta) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(delta);
			
			//name has changed?
			if (!delta.name().equals(name()))
				this.name=delta.name();
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Named.Abstract<?> other = (Named.Abstract<?>) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
	}
}

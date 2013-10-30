package org.cotrix.domain.trait;

import static org.cotrix.domain.utils.Constants.*;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.NamedPO;

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
		
		protected void fillPO(boolean withId,NamedPO po) {
			super.fillPO(withId,po);
			po.setName(name());
		}
		
		@Override
		public QName name() {
			return name;
		}
		
		@Override
		public void update(T changeset) throws IllegalArgumentException ,IllegalStateException {
			
			super.update(changeset);
			
			if (changeset.name()!=null)
				if (changeset.name()==NULL_QNAME)
					throw new IllegalArgumentException("code name "+name+" cannot be erased");
				else
					name=changeset.name();
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

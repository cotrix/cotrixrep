package org.cotrix.domain;

import javax.xml.namespace.QName;

import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.spi.IdGenerator;
import org.cotrix.domain.trait.Identified;
import org.cotrix.domain.trait.Named;


/**
 * A named and typed attribute for a domain object.
 * 
 * @author Fabio Simeoni
 *
 */
public interface Attribute extends Identified, Named {

	/**
	 * Returns the name of the attribute.
	 * @return the name
	 */
	QName name();
	
	/**
	 * Returns the type of the attribute.
	 * @return the type
	 */
	QName type();

	/**
	 * Returns the value of the attribute
	 * @return the value
	 */
	String value();
	
	
	/**
	 * {@link Attribute} implementation.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public class Private extends Identified.Abstract<Private> implements Attribute {

		private QName name;
		private QName type;
		private String value;
		
		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		public Private(AttributePO params) {
			
			super(params);
			
			this.name=params.name();
			this.type=params.type();
			this.value=params.value();
		}
		
		@Override
		public QName name() {
			return name;
		}
		
		@Override
		public QName type() {
			return type;
		}
	
		@Override
		public String value() {
			return value;
		}
		
		
		protected void fillPO(AttributePO po) {
			po.setName(name);
			po.setType(type());
			po.setValue(value());
		}
		
		@Override
		public Private copy(IdGenerator generator) {
			AttributePO po = new AttributePO(generator.generateId());
			fillPO(po);
			return new Private(po);
		}
		
		@Override
		public void update(Attribute.Private delta) throws IllegalArgumentException, IllegalStateException {
			
			super.update(delta);
			
			name=delta.name();
			type=delta.type();
			value=delta.value();
			
		}
	
		@Override
		public String toString() {
			return "Attribute [id=" +id()+", name=" + name() + ", value=" + value + (type==null?"":", type=" + type)+ (change()==null?"":"("+change()+")")+"]";
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
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
			Private other = (Private) obj;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
	}
}
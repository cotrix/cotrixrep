package org.cotrix.domain;

import static org.cotrix.domain.utils.Constants.*;

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
	 * Returns the language of the attribute
	 * @return the language
	 */
	String language();
	
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
		private String language;
		
		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		public Private(AttributePO params) {
			
			super(params);
			
			this.name=params.name();
			this.type=params.type();
			this.value=params.value();
			this.language=params.language();
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
		
		@Override
		public String language() {
			return language;
		}
		
		protected void fillPO(AttributePO po) {
			po.setName(name);
			po.setType(type);
			po.setValue(value);
			if (language!=null)
				po.setLanguage(language);
		}
		
		@Override
		public Private copy(IdGenerator generator) {
			AttributePO po = new AttributePO(generator.generateId());
			fillPO(po);
			return new Private(po);
		}
		
		@Override
		public void update(Attribute.Private changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
			
			if (changeset.name()!=null)
				if (changeset.name()==NULL_QNAME)
					throw new IllegalArgumentException("attribute name "+name+" cannot be erased");
				else
					name=changeset.name();
			
			if (changeset.type()!=null)
				type = changeset.type()==NULL_QNAME ? null: changeset.type();
			
			if (changeset.value()!=null)
				value = changeset.value()==NULL_STRING ? null: changeset.value();
			
			if (changeset.language()!=null)
				language = changeset.language()==NULL_STRING ? null: changeset.language();
			
			
		}
	
		@Override
		public String toString() {
			return "Attribute [id=" +id()+", name=" + name() + ", value=" + value + ", language=" + language + (type==null?"":", type=" + type)+ (status()==null?"":" ("+status()+") ")+"]";
		}
	
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((language == null) ? 0 : language.hashCode());
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
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
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
			if (language == null) {
				if (other.language != null)
					return false;
			} else if (!language.equals(other.language))
				return false;
			return true;
		}
	}
}
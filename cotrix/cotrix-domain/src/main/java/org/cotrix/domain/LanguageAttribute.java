package org.cotrix.domain;

import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.spi.IdGenerator;

/**
 * An {@link Attribute} with a value in a given language.
 * 
 * @author Fabio Simeoni
 *
 */
public interface LanguageAttribute extends Attribute {

	/**
	 * Returns the language of the attribute
	 * @return the language
	 */
	String language();
	
	/**
	 * An {@link Attribute.Private} implementation of {@link LanguageAttribute}.
	 * 
	 * @author Fabio Simeoni
	 *
	 */
	public class Private extends Attribute.Private implements LanguageAttribute {
		
		private String language;
		
		/**
		 * Creates a new instance from a given set of parameters.
		 * @param params the parameters
		 */
		public Private(AttributePO params) {
			super(params);
			this.language=params.language();
		}
		
		@Override
		public String language() {
			return language;
		}

		@Override
		public LanguageAttribute.Private copy(IdGenerator generator) {
			AttributePO po = new AttributePO(generator.generateId());
			fillPO(po);
			po.setLanguage(language());
			return new LanguageAttribute.Private(po);
		}
		
		@Override
		public void update(Attribute.Private delta) throws IllegalArgumentException, IllegalStateException {
				
				super.update(delta);
				
				if (delta instanceof LanguageAttribute)
					this.language=((LanguageAttribute) delta).language();
				
		}
		

		@Override
		public String toString() {
				return "LanguageAttribute [id=" +id()+", language=" + language +", name=" + name() + ", value=" + value() + (type()==null?"":", type=" + type())+"]";
			
		}


		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
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
			LanguageAttribute.Private other = (LanguageAttribute.Private) obj;
			if (language == null) {
				if (other.language != null)
					return false;
			} else if (!language.equals(other.language))
				return false;
			return true;
		}
	}

}

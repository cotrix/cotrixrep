package org.cotrix.domain.simple.attribute;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.LanguageAttribute;
import org.cotrix.domain.po.AttributePO;
import org.cotrix.domain.utils.IdGenerator;

/**
 * Default {@link LanguageAttribute} implementation.
 * 
 * @author Fabio Simeoni
 *
 */
public class SimpleLanguageAttribute extends SimpleAttribute implements LanguageAttribute {

	private String language;
	
	public SimpleLanguageAttribute(AttributePO params) {
		super(params);
		this.language=params.language();
	}
	
	@Override
	public String language() {
		return language;
	}

	@Override
	public SimpleLanguageAttribute copy(IdGenerator generator) {
		AttributePO po = new AttributePO(generator.generateId());
		fillPO(po);
		po.setLanguage(language());
		return new SimpleLanguageAttribute(po);
	}
	
	@Override
	public void update(Attribute delta) throws IllegalArgumentException, IllegalStateException {
			
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
		SimpleLanguageAttribute other = (SimpleLanguageAttribute) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
}

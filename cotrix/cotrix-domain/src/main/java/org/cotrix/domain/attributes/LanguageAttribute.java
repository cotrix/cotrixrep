package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Utils.*;

import javax.xml.namespace.QName;

public class LanguageAttribute extends Attribute {

	private final String language;
	
	public LanguageAttribute(QName name, String value,String language) {
		this(name,null,value,language);
	}
	
	public LanguageAttribute(QName name, QName type, String value, String language) {
		super(name,type,value);
		
		valid("language",language);
		this.language=language;
	}
	
	public String language() {
		return language;
	}

	@Override
	public LanguageAttribute copy() { //fragile
		return new LanguageAttribute(name(), type(),value(),language());
	}

	@Override
	public String toString() {
			return "LocaleAttribute [language=" + language +", name=" + name() + ", value=" + value() + (type()==null?"":", type=" + type())+"]";
		
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
		LanguageAttribute other = (LanguageAttribute) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
}

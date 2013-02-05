package org.cotrix.domain.attributes;

import static org.cotrix.domain.utils.Utils.*;

import java.util.Locale;
import java.util.MissingResourceException;

import javax.xml.namespace.QName;

public class BaseAttribute implements Attribute {

	private final QName name;
	private final String value;
	private final String language;
	
	BaseAttribute(QName name,String value,Locale language) throws IllegalArgumentException {
		
		valid(name);
		this.name=name;
		
		notNullorEmpty("value",value);
		this.value=value;
		
		notNull("language",language);
		try {
			this.language=language.getISO3Language();
		}
		catch(MissingResourceException e) {
			throw new IllegalArgumentException("unknown ISO3 code for "+language);
		}
	}
	
	public QName name() {
		return name;
	}

	public String value() {
		return value;
	}

	public String language() {
		return language;
	}

	
}

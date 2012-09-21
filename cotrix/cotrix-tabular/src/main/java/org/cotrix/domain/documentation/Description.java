package org.cotrix.domain.documentation;

import java.util.Locale;

/**
 * A description in a certain language
 * 
 * @author Erik van Ingen
 * 
 */
public class Description {

	protected String value;
	protected Locale language;

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}

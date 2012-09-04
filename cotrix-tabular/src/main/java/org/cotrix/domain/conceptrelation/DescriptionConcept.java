package org.cotrix.domain.conceptrelation;

import java.util.Locale;

/**
 * A descriptionConcept is the description in a certain language of another concept. The other concept is not modeled in
 * this class but is known through the concept relation
 * 
 * 
 * 
 * @author Erik van Ingen
 * 
 */
public class DescriptionConcept extends Concept {

	protected Locale language;

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

}

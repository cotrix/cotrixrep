package org.cotrix.domain;

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

}

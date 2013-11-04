package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeleteClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeGrammar {

	
	public static interface AttributeStartClause extends NameClause<ValueClause> {	}
		
	public static interface AttributeDeltaClause extends DeleteClause<Attribute>, NameClause<ValueClause>, ValueClause, TypeClause, LanguageClause {}

	public static interface ValueClause extends BuildClause<Attribute> {
		
		/**
		 * Sets the value of the object.
		 * 
		 * @param value the value
		 * @return the next clause in the sentence
		 */
		TypeClause value(String value);
	}

	public static interface TypeClause extends LanguageClause {

		/**
		 * Sets the type of the object.
		 * 
		 * @param type the type
		 * @return the next clause in the sentence
		 */
		LanguageClause ofType(QName type);
		
		/**
		 * Sets the type of the object.
		 * 
		 * @param type the type
		 * @return the next clause in the sentence
		 */
		LanguageClause ofType(String type);
	}

	public static interface LanguageClause extends BuildClause<Attribute> {

		/**
		 * Sets the language of the object.
		 * 
		 * @param language the language
		 * @return the next clause in the sentence
		 */
		BuildClause<Attribute> in(String language);
	}
}

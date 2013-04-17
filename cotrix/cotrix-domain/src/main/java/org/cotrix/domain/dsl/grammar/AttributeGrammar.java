package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.Attribute;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeGrammar {

	public static interface AttributeStartClause extends NameClause<SecondClause> {

	}

	public static interface SecondClause {
		
		/**
		 * Sets the value of the object.
		 * 
		 * @param value the value
		 * @return the next clause in the sentence
		 */
		ThirdClause value(String value);
	}

	public static interface ThirdClause extends FinalClause, BuildClause<Attribute> {

		/**
		 * Sets the type of the object.
		 * 
		 * @param type the type
		 * @return the next clause in the sentence
		 */
		FinalClause ofType(QName type);
		
		/**
		 * Sets the type of the object.
		 * 
		 * @param type the type
		 * @return the next clause in the sentence
		 */
		FinalClause ofType(String type);
	}

	public static interface FinalClause extends BuildClause<Attribute> {

		/**
		 * Sets the language of the object.
		 * 
		 * @param language the language
		 * @return the next clause in the sentence
		 */
		BuildClause<Attribute> in(String language);
	}
}

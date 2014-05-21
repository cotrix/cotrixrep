package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeGrammar {

	
	public static interface AttributeNewClause extends NameClause<OptionalClause>, TypeClause {	
		
		ValueClause instanceOf(AttributeType type);
	}
		
	
	
	public static interface AttributeChangeClause extends NameClause<OptionalClause>, OptionalClause, TypeClause {
		
		
	}
	
	public static interface TypeClause {
		
		ValueClause instanceOf(AttributeType type);
	}
	
	
	public static interface ValueClause extends BuildClause<Attribute> {

		OptionalClause value(String value);
	}
	
	
	public static interface OptionalClause extends ValueClause {
		
		
		OptionalClause ofType(QName type);
		
		OptionalClause ofType(String type);
	
		OptionalClause in(String language);
	}

}

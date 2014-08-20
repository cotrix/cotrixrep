package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeGrammar {

	
	public static interface AttributeNewClause extends NameClause<FourthClause>, SecondClause {	
		
		ThirdClause instanceOf(AttributeDefinition definition);
	}
		
	
	
	public static interface AttributeChangeClause extends NameClause<FourthClause>, SecondClause, ThirdClause, FourthClause {
		
		
	}
	
	public static interface SecondClause {
		
		ThirdClause instanceOf(AttributeDefinition definition);
		
		ThirdClause instanceOf(CommonDefinition definition);
	}
	
	
	public static interface ThirdClause extends BuildClause<Attribute> {

		FourthClause value(String value);
	}
	
	
	public static interface FourthClause extends ThirdClause {
		
		
		FourthClause ofType(QName type);
		
		FourthClause ofType(String type);
	
		FourthClause in(String language);
		
		FourthClause description(String description);
	}

}

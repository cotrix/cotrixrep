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

	
	public static interface AttributeNewClause extends NameClause<OptionalClause>, DefinitionClause {	
		
		ValueClause with(AttributeDefinition definition);
	}
		
	
	
	public static interface AttributeChangeClause extends NameClause<OptionalClause>, OptionalClause, DefinitionClause {
		
		
	}
	
	public static interface DefinitionClause {
		
		ValueClause with(AttributeDefinition definition);
		
		ValueClause with(CommonDefinition definition);
	}
	
	
	public static interface ValueClause extends BuildClause<Attribute> {

		OptionalClause value(String value);
	}
	
	
	public static interface OptionalClause extends ValueClause {
		
		
		OptionalClause ofType(QName type);
		
		OptionalClause ofType(String type);
	
		OptionalClause in(String language);
		
		OptionalClause description(String description);
	}

}

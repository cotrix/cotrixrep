package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.values.ValueType;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class DefinitionGrammar {

	
	public static interface DefinitionNewClause extends NameClause<OptionalClause> {	}
		
	public static interface DefinitionChangeClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface OptionalClause extends BuildClause<AttributeDefinition> {
		
		OptionalClause is(QName type);
		
		OptionalClause is(String type);
		
		OptionalClause valueIs(ValueType type);
	
		OptionalClause in(String language);
		
		OptionalClause occurs(Range range);
	}

}

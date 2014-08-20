package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.values.ValueType;


public class AttributeDefinitionGrammar {

	
	public static interface AttributeDefinitionNewClause extends NameClause<SecondClause> {	}
		
	public static interface AttributeDefinitionChangeClause extends NameClause<SecondClause>, SecondClause {}
	
	public static interface SecondClause extends BuildClause<AttributeDefinition> {
		
		SecondClause is(QName type);
		
		SecondClause is(String type);
		
		SecondClause valueIs(ValueType type);
	
		SecondClause in(String language);
		
		SecondClause occurs(Range range);
	}

}

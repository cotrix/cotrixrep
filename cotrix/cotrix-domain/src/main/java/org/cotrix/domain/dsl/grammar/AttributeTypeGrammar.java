package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.AttributeType;
import org.cotrix.domain.common.AttributeValueType;
import org.cotrix.domain.common.OccurrenceRange;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeTypeGrammar {

	
	public static interface AttributeTypeStartClause extends NameClause<OptionalClause> {	}
		
	public static interface AttributeTypeDeltaClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface OptionalClause extends BuildClause<AttributeType> {
		
		OptionalClause ofType(QName type);
		
		OptionalClause ofType(String type);
		
		OptionalClause withValuesOfType(AttributeValueType type);
	
		OptionalClause in(String language);
		
		OptionalClause occurs(OccurrenceRange range);
	}

}

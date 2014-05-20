package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeType;
import org.cotrix.domain.attributes.AttributeValueType;
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

	
	public static interface AttributeTypeNewClause extends NameClause<OptionalClause> {	}
		
	public static interface AttributeTypeChangeClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface OptionalClause extends BuildClause<AttributeType> {
		
		OptionalClause ofType(QName type);
		
		OptionalClause ofType(String type);
		
		OptionalClause valuesIs(AttributeValueType type);
	
		OptionalClause in(String language);
		
		OptionalClause occurs(OccurrenceRange range);
	}

}

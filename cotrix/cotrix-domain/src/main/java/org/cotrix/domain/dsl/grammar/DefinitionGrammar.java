package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.attributes.ValueType;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class DefinitionGrammar {

	
	public static interface AttributeTypeNewClause extends NameClause<OptionalClause> {	}
		
	public static interface AttributeTypeChangeClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface OptionalClause extends BuildClause<Definition> {
		
		OptionalClause ofType(QName type);
		
		OptionalClause ofType(String type);
		
		OptionalClause valuesIs(ValueType type);
	
		OptionalClause in(String language);
		
		OptionalClause occurs(Range range);
	}

}

package org.cotrix.domain.dsl.grammar;

import javax.xml.namespace.QName;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Attribute}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class AttributeGrammar {

	
	public static interface AttributeStartClause extends NameClause<OptionalClause> {	}
		
	public static interface AttributeDeltaClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface OptionalClause extends BuildClause<Attribute> {
		
		OptionalClause value(String value);
		
		OptionalClause ofType(QName type);
		
		OptionalClause ofType(String type);
	
		OptionalClause in(String language);
	}

}

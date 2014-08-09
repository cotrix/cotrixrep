package org.cotrix.domain.dsl.grammar;


import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.values.ValueFunction;


public class LinkDefinitionGrammar {

	public static interface LinkDefinitionNewClause extends NameClause<LinkTargetClause<Codelist,OptionalClause>>{}
	
	public static interface LinkDefinitionChangeClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface ValueTypeClause {
		
		OptionalClause anchorToName();
		
		OptionalClause anchorTo(Attribute template);
		
		OptionalClause anchorTo(LinkDefinition template);
	}

	public static interface OptionalClause extends ValueTypeClause, AttributeClause<LinkDefinition, OptionalClause> {
		
		OptionalClause transformWith(ValueFunction function);
		
		OptionalClause occurs(Range range);
	}
}

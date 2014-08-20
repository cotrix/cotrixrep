package org.cotrix.domain.dsl.grammar;


import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Range;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.values.ValueFunction;


public class LinkDefinitionGrammar {

	public static interface LinkDefinitionNewClause extends NameClause<SecondClause>{}
	
	public static interface LinkDefinitionChangeClause extends NameClause<ThirdClause>, ThirdClause {}
	
	public static interface SecondClause {

		ThirdClause target(Codelist target);
	}
	
	public static interface ThirdClause extends AttributeClause<LinkDefinition, ThirdClause>, BuildClause<LinkDefinition> {
		
		ThirdClause anchorToName();
		
		ThirdClause anchorTo(Attribute template);
		
		ThirdClause anchorTo(LinkDefinition template);
		
		ThirdClause transformWith(ValueFunction function);
		
		ThirdClause occurs(Range range);
	}
}

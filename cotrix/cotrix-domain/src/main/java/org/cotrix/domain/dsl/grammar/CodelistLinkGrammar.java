package org.cotrix.domain.dsl.grammar;


import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.OccurrenceRange;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.links.ValueFunction;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodelistLinkGrammar {

	public static interface CodelistLinkNewClause extends NameClause<LinkTargetClause<Codelist,OptionalClause>>{}
	
	public static interface CodelistLinkChangeClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface ValueTypeClause {
		
		OptionalClause anchorToName();
		
		OptionalClause anchorTo(Attribute template);
		
		OptionalClause anchorTo(CodelistLink template);
	}

	public static interface OptionalClause extends ValueTypeClause, AttributeClause<CodelistLink, OptionalClause> {
		
		OptionalClause transformWith(ValueFunction function);
		
		OptionalClause occurs(OccurrenceRange range);
	}
}

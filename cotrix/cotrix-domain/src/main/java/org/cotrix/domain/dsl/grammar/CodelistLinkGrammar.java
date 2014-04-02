package org.cotrix.domain.dsl.grammar;


import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodelistLinkGrammar {

	public static interface CodelistLinkNewClause extends NameClause<OptionalClause>{}
	
	public static interface CodelistLinkChangeClause extends NameClause<OptionalClause>, OptionalClause {}
	
	public static interface TypeClause {
		
		OptionalClause onName();
		
		OptionalClause onAttribute(Attribute template);
		
		OptionalClause onLink(CodelistLink template);
	}

	public static interface OptionalClause extends LinkTargetClause<Codelist,OptionalClause>,TypeClause, AttributeClause<CodelistLink, OptionalClause> {
	}
}

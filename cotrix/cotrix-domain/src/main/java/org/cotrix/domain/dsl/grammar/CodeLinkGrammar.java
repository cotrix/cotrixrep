package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Code;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.CodeLinkClause;
import org.cotrix.domain.primitive.link.CodeLink;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodeLinkGrammar {

	public static interface CodeLinkStartClause extends CodeLinkClause<FinalClause> {
	}

	public static interface FinalClause extends AttributeClause<CodeLink,FinalClause> {
	}
}

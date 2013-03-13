package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.CodeLinkClause;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodeLinkGrammar {

	public static interface CodeLinkStartClause extends CodeLinkClause<FinalClause> {
	}

	public static interface FinalClause extends AttributeClause<Codelink,FinalClause> {
	}
}
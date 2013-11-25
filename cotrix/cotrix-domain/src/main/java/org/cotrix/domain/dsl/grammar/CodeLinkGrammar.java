package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.CodeLinkClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeleteClause;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodeLinkGrammar {

	public static interface CodeLinkStartClause extends CodeLinkClause<FinalClause>, DeleteClause<Codelink> {
	}
	
	public static interface FinalClause extends AttributeClause<Codelink,FinalClause> {
	}
}

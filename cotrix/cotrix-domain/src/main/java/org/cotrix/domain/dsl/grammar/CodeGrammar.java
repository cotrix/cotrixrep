package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Code;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodeGrammar {

	public static interface CodeStartClause extends NameClause<FinalClause>  {}

	public static interface FinalClause extends AttributeClause<Code, FinalClause> {}
}

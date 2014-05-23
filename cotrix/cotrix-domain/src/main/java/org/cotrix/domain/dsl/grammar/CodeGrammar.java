package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinksClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodeGrammar {

	public static interface CodeNewClause extends NameClause<OptionalClause>  {}
	
	public static interface CodeChangeClause extends NameClause<OptionalClause>, OptionalClause {}

	public static interface OptionalClause extends LinksClause<Codelink,OptionalClause>, AttributeClause<Code,OptionalClause> {}
}

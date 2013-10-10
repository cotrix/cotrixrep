package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelink;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeltaClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinksClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;

/**
 * The grammar of DSL sentences that create {@link Code}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodeGrammar {

	public static interface CodeStartClause extends NameClause<SecondClause>, DeltaClause<NameClause<SecondClause>,ChangeClause,Code>  {}
	
	public static interface ChangeClause extends NameClause<SecondClause>, SecondClause, FinalClause {}

	public static interface SecondClause extends LinksClause<Codelink,FinalClause>,AttributeClause<Code,FinalClause> {}
	
	public static interface FinalClause extends AttributeClause<Code, FinalClause> {}
}

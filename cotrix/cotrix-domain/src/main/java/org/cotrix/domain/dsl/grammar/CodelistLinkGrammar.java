package org.cotrix.domain.dsl.grammar;


import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
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

	public static interface CodelistLinkStartClause extends NameClause<SecondClause>  {}

	public static interface SecondClause extends AttributeClause<CodelistLink, FinalClause>,LinkTargetClause<Codelist,FinalClause> {}
	
	public static interface FinalClause extends AttributeClause<CodelistLink, FinalClause> {}
}

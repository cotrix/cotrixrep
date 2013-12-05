package org.cotrix.domain.dsl.grammar;


import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
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

	public static interface CodelistLinkNewClause extends NameClause<SecondClause>  {}
	
	public static interface CodelistLinkChangeClause extends NameClause<SecondClause>, SecondClause, FinalClause {}

	public static interface SecondClause extends AttributeClause<CodelistLink, FinalClause>,LinkTargetClause<Codelist,FinalClause> {}
	
	public static interface FinalClause extends AttributeClause<CodelistLink, FinalClause> {}
}

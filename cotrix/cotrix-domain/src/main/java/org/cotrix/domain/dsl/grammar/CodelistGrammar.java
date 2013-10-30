package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.CodelistLink;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.DeleteClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinksClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.VersionClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.WithManyClause;

/**
 * The grammar of DSL sentences that create {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 * 
 */
@SuppressWarnings("all")
public class CodelistGrammar {

	public static interface CodelistNewClause extends NameClause<SecondClause> {}
	
	public static interface CodelistChangeClause extends DeleteClause<Codelist>, NameClause<SecondClause>, SecondClause {}
	
	public static interface SecondClause extends WithManyClause<Code,ThirdClause>,
												 AttributeClause<Codelist,FinalClause>,
												 VersionClause<Codelist> {}

	public static interface ThirdClause extends LinksClause<CodelistLink,FourthClause>,AttributeClause<Codelist,FinalClause>,VersionClause<Codelist> {}

	public static interface FourthClause extends AttributeClause<Codelist,FinalClause>,VersionClause<Codelist> {}
	
	public static interface FinalClause extends VersionClause<Codelist>, BuildClause<Codelist> {}
	

	
}

package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinksClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.VersionClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.WithManyClause;
import org.cotrix.domain.primitive.link.CodelistLink;

/**
 * The grammar of DSL sentences that create {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodelistGrammar {

	public static interface CodelistStartClause extends NameClause<SecondClause> {}

	public static interface SecondClause extends WithManyClause<Code,ThirdClause>,
												 AttributeClause<Codelist,FinalClause>,
												 VersionClause<Codelist> {}

	public static interface ThirdClause extends LinksClause<CodelistLink,FourthClause>,AttributeClause<Codelist,FinalClause>,VersionClause<Codelist> {}

	public static interface FourthClause extends AttributeClause<Codelist,FinalClause>,VersionClause<Codelist> {}
	
	public static interface FinalClause extends VersionClause<Codelist>, BuildClause<Codelist> {}
}

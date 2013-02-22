package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.VersionClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.WithManyClause;

/**
 * The grammar of DSL sentences that create {@link Codebag}s.
 * 
 * @author Fabio Simeoni
 * 
 */
public class CodebagGrammar {

	public static interface CodebagStartClause extends NameClause<SecondClause> {

	}

	public static interface SecondClause extends WithManyClause<Codelist,ThirdClause>,
												 AttributeClause<Codebag,FinalClause>,
												 VersionClause<Codebag> {}

	public static interface ThirdClause extends AttributeClause<Codebag,FinalClause>,VersionClause<Codebag> {}

	public static interface FinalClause extends VersionClause<Codebag>, BuildClause<Codebag> {}
}

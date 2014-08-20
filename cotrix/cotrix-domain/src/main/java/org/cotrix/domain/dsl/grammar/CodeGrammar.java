package org.cotrix.domain.dsl.grammar;

import java.util.Collection;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.links.Link;

public class CodeGrammar {

	public static interface CodeNewClause extends NameClause<SecondClause>  {}
	
	public static interface CodeChangeClause extends NameClause<SecondClause>, SecondClause {}

	public static interface SecondClause extends AttributeClause<Code,SecondClause>, BuildClause<Code> {

		SecondClause links(Link ... links);
		
		SecondClause links(Collection<Link> links);

	}
}

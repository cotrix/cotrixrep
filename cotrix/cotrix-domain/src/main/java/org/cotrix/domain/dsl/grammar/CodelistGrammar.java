package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinksClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.VersionClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.WithManyClause;
import org.cotrix.domain.links.LinkDefinition;

/**
 * The grammar of DSL sentences that create {@link Codelist}s.
 * 
 * @author Fabio Simeoni
 * 
 */
@SuppressWarnings("all")
public class CodelistGrammar {

	public static interface CodelistNewClause extends NameClause<SecondClause> {}
	
	//name is mandatory
	public static interface CodelistChangeClause extends NameClause<SecondClause>, SecondClause {}
	
	//all the other are optionals (collections or defaultable)
	public static interface SecondClause extends WithManyClause<Code,SecondClause>,
												 LinksClause<LinkDefinition,SecondClause>,
												 AttributeClause<Codelist,SecondClause>,
												 VersionClause<Codelist> {
		
		
		
		SecondClause definitions(AttributeDefinition ... types);
		
		
		SecondClause definitions(Iterable<AttributeDefinition> types);
	}

	

	
}

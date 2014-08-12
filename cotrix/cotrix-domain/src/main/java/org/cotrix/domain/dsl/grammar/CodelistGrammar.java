package org.cotrix.domain.dsl.grammar;

import java.util.Collection;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinksClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.VersionClause;
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
	public static interface SecondClause extends LinksClause<LinkDefinition,SecondClause>,
												 AttributeClause<Codelist,SecondClause>,
												 VersionClause<Codelist> {
		
		
		
		SecondClause definitions(AttributeDefinition ... defs);
		
		SecondClause definitions(AttributeDefinitionGrammar.OptionalClause ... defs);
		
		SecondClause definitions(Iterable<AttributeDefinition> defs);
		
		SecondClause with(Code ... codes);
		
		SecondClause with(CodeGrammar.OptionalClause ... codes);
		
		SecondClause with(Iterable<Code> codes);
	}

	

	
}

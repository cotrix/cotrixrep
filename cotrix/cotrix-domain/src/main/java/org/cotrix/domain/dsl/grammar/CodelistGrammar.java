package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.NameClause;
import org.cotrix.domain.links.LinkDefinition;

public class CodelistGrammar {

	public static interface CodelistNewClause extends NameClause<SecondClause> {}
	
	public static interface CodelistChangeClause extends NameClause<SecondClause>, SecondClause {}
	
	public static interface SecondClause extends AttributeClause<Codelist,SecondClause>,
												 BuildClause<Codelist>{
		
		
		
		SecondClause definitions(AttributeDefinition ... defs);
		
		SecondClause definitions(AttributeDefinitionGrammar.SecondClause ... defs);
		
		SecondClause definitions(Iterable<AttributeDefinition> defs);
		
		
		SecondClause links(LinkDefinition ... defs);
		
		SecondClause links(LinkDefinitionGrammar.ThirdClause ... defs);
		
		SecondClause links(Iterable<LinkDefinition> defs);
		
		
		SecondClause with(Code ... codes);
		
		SecondClause with(CodeGrammar.SecondClause ... codes);
		
		SecondClause with(Iterable<Code> codes);
		
		
		SecondClause version(String version);
	}

}

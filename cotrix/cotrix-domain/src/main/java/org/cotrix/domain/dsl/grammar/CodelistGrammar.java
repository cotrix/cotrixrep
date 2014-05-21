package org.cotrix.domain.dsl.grammar;

import java.util.Collection;

import org.cotrix.domain.attributes.Definition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.codelist.CodelistLink;
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
	
	//name is mandatory
	public static interface CodelistChangeClause extends NameClause<SecondClause>, SecondClause {}
	
	//all the other are optionals (collections or defaultable)
	public static interface SecondClause extends WithManyClause<Code,SecondClause>,
												 LinksClause<CodelistLink,SecondClause>,
												 AttributeClause<Codelist,SecondClause>,
												 VersionClause<Codelist> {
		
		
		
		SecondClause definitions(Definition ... types);
		
		
		SecondClause attributeTypes(Collection<Definition> types);
	}

	

	
}

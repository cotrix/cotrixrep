package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.BuildClause;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;

public class LinkGrammar {

	public static interface CodelinkNewClause {
		
		SecondClause instanceOf(LinkDefinition type);
	}
	
	public static interface SecondClause {

		ThirdClause target(Code target);
	}
	
	public static interface CodelinkChangeClause extends SecondClause, ThirdClause {}
	
	
	public static interface ThirdClause extends AttributeClause<Link,ThirdClause>, BuildClause<Link> {
		
		
	}
}

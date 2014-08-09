package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Link;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;
import org.cotrix.domain.links.LinkDefinition;

public class CodelinkGrammar {

	public static interface CodelinkNewClause {
		
		LinkTargetClause<Code,OptionalClause> instanceOf(LinkDefinition type);
	}
	
	public static interface CodelinkChangeClause extends LinkTargetClause<Code,OptionalClause>, OptionalClause {}
	
	
	public static interface OptionalClause extends AttributeClause<Link,OptionalClause> {
	}
}

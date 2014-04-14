package org.cotrix.domain.dsl.grammar;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.CodelistLink;
import org.cotrix.domain.dsl.grammar.CommonClauses.AttributeClause;
import org.cotrix.domain.dsl.grammar.CommonClauses.LinkTargetClause;

public class CodelinkGrammar {

	public static interface CodelinkNewClause {
		
		LinkTargetClause<Code,OptionalClause> instanceOf(CodelistLink type);
	}
	
	public static interface CodelinkChangeClause extends LinkTargetClause<Code,OptionalClause>, OptionalClause {}
	
	
	public static interface OptionalClause extends AttributeClause<Codelink,OptionalClause> {
	}
}

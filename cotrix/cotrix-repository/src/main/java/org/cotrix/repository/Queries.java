package org.cotrix.repository;

import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.memory.MQueryFactory;
import org.cotrix.repository.query.CodelistQuery;

public class Queries {

	private static QueryFactory factory = new MQueryFactory();
	
	public static void setFactory(QueryFactory factory) {
		Queries.factory = factory;
	}
	
	public static CodelistQuery<Codelist> allLists() {
		return factory.allLists();
	}
	
	public static CodelistQuery<Code> allCodes(String codelistId) {
		return factory.allCodes(codelistId);
	}
}

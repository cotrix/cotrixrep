package org.cotrix.repository;

import org.cotrix.domain.Codebag;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.memory.MQueryFactory;
import org.cotrix.repository.query.Query;

public class Queries {

	private static QueryFactory factory = new MQueryFactory();
	
	public static void setFactory(QueryFactory factory) {
		Queries.factory = factory;
	}
	
	public static Query<Codelist,Codelist> lists() {
		return factory.allLists();
	}
	
	public static Query<Codebag,Codebag> bags() {
		return factory.allBags();
	}
}

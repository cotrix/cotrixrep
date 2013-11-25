package org.cotrix.repository;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.domain.Code;
import org.cotrix.domain.Codelist;
import org.cotrix.repository.memory.MQueryFactory;

public class CodelistQueries {

	private static QueryFactory factory = new MQueryFactory();
	
	public static void setFactory(QueryFactory factory) {
		CodelistQueries.factory = factory;
	}
	
	public static CodelistQuery<Codelist> allLists() {
		return factory.allLists();
	}
	
	public static CodelistQuery<Code> allCodes(String codelistId) {
		return factory.allCodes(codelistId);
	}
	
	public static CodelistQuery<CodelistCoordinates> allListCoordinates() {
		return factory.allListCoordinates();
	}
	
	public static Specification<Codelist,CodelistSummary> summary(String id) {
		return factory.summary(id);
	}
	
	static class QueryFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, QueryFactory factory) {	
			
			CodelistQueries.setFactory(factory);
		}
	}
}

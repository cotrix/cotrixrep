package org.cotrix.repository.codelist;

import javax.enterprise.event.Observes;

import org.cotrix.common.cdi.ApplicationEvents;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.repository.MultiQuery;
import org.cotrix.repository.Query;
import org.cotrix.repository.codelist.impl.CodelistQueryFactory;
import org.cotrix.repository.codelist.impl.MCodelistQueryFactory;

public class CodelistQueries {

	private static CodelistQueryFactory factory = new MCodelistQueryFactory();
	
	public static void setFactory(CodelistQueryFactory factory) {
		CodelistQueries.factory = factory;
	}
	
	public static MultiQuery<Codelist,Codelist> allLists() {
		return factory.allLists();
	}
	
	public static MultiQuery<Codelist,Code> allCodesIn(String codelistId) {
		return factory.allCodes(codelistId);
	}
	
	public static MultiQuery<Codelist,CodelistCoordinates> allListCoordinates() {
		return factory.allListCoordinates();
	}
	
	public static Query<Codelist,CodelistSummary> summary(String id) {
		return factory.summary(id);
	}
	
	static class QueryFactoryInjector {

		void configure(@Observes ApplicationEvents.Startup event, CodelistQueryFactory factory) {	
			
			CodelistQueries.setFactory(factory);
		}
	}
}

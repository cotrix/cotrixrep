package org.cotrix.repository;

import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.event.Observes;
import javax.xml.namespace.QName;

import org.cotrix.common.events.ApplicationLifecycleEvents;
import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.CommonDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.trait.Named;
import org.cotrix.domain.user.User;
import org.cotrix.repository.spi.CodelistQueryFactory;

public class CodelistQueries {

	private static CodelistQueryFactory factory;
	
	public static void setFactory(CodelistQueryFactory factory) {
		CodelistQueries.factory = factory;
	}
	
	public static MultiQuery<Codelist,Codelist> allLists() {
		return factory.allLists();
	}
	
	public static MultiQuery<Codelist,Code> allCodesIn(String codelistId) {
		return factory.allCodes(codelistId);
	}
	
	public static Query<Codelist,Code> code(String id) {
		return factory.code(id);
	}
	
	public static MultiQuery<Codelist,Code> codes(Collection<String> id) {
		return factory.codes(id);
	}
	
	public static CodelistClause codesWithAttributes(final Iterable<QName> names) {
		return new CodelistClause() {
			
			@Override
			public MultiQuery<Codelist, Code> in(String id) {
				return factory.codesWithAttributes(id,names);
			}
		};
	}
	
	public static CodelistClause codesWithCommonAttributes(final Iterable<QName> names) {
		return new CodelistClause() {
			
			@Override
			public MultiQuery<Codelist, Code> in(String id) {
				return factory.codesWithCommonAttributes(id,names);
			}
		};
	}
	
	public static CodelistClause codesWith(final Named ... named) {
		return new CodelistClause() {
			
			@Override
			public MultiQuery<Codelist, Code> in(String id) {
				
				Collection<QName> names = new ArrayList<>();
				for (Named n : named)
					names.add(n.qname());
				
				return factory.codesWithAttributes(id,names);
			}
		};
	}
	
	public static CodelistClause codesWith(final CommonDefinition ... defs) {
		return new CodelistClause() {
			
			@Override
			public MultiQuery<Codelist, Code> in(String id) {
				
				Collection<QName> names = new ArrayList<>();
				for (Named n : defs)
					names.add(n.qname());
				
				return factory.codesWithCommonAttributes(id,names);
			}
		};
	}
	
	public static interface CodelistClause {
		
		MultiQuery<Codelist,Code> in(String id);
	}
	
	public static MultiQuery<Codelist,CodelistCoordinates> codelistsFor(User u) {
		return factory.codelistsFor(u);
	}
	
	public static MultiQuery<Codelist,CodelistCoordinates> allListCoordinates() {
		return factory.allListCoordinates();
	}
	
	public static Query<Codelist,CodelistSummary> summary(String id) {
		return factory.summary(id);
	}
	
	public static Criterion<Codelist> byCodelistName() {
		
		return factory.byCodelistName();
	}
	
	public static Criterion<Codelist> byVersion() {
		
		return factory.byVersion();
	}
	
	public static Criterion<Code> byCodeName() {
		
		return factory.byCodeName();
	}
	
	public static Criterion<CodelistCoordinates> byCoordinateName() {
		
		return factory.byCoordinateName();
	}
	
	public static <T> Criterion<T> all(Criterion<T> c1, Criterion<T> c2) {
		
		return factory.all(c1,c2);
	}
	
	public static <T> Criterion<T> descending(Criterion<T> c) {
		
		return factory.descending(c);
	}
	
	public static Criterion<Code> byAttribute(final Attribute attribute, int position) {
		return factory.byAttribute(attribute,position);
	}
	
	static class QueryFactoryInjector {

		void configure(@Observes ApplicationLifecycleEvents.Startup event, CodelistQueryFactory factory) {	
			
			CodelistQueries.setFactory(factory);
		}
	}
}

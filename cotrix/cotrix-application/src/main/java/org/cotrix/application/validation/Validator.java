package org.cotrix.application.validation;

import static java.lang.String.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.attributes.Facet.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.common.NamedContainer;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.managed.ManagedCode;
import org.cotrix.domain.trait.Defined;
import org.cotrix.domain.trait.Identified;

@Singleton
public class Validator {

	static String  occurrenceViolation = "%s occurs %s time(s), violating occurences constraint %s";


	public void check(Iterable<Code> codes) {
	
		notNull("codes",codes);
		
		for (Code code : codes)
			check(reveal(code));
	}
	
	private void check(Code.Private code) {
		
		NamedStateContainer<Attribute.State> attributes = code.state().attributes();
		
		List<String> errors = new ArrayList<>();
		
		errors.addAll(checkAttributes(code.attributes()));
		errors.addAll(checkLinks(code.links()));
		
		ManagedCode managed = manage(code);
		
		Attribute invalid = managed.attribute(INVALID);
		
		if (errors.isEmpty()) {
			
			if (invalid!=null)
				attributes.remove(invalid.id());
			
			return;
		}
	
		
		if (invalid==null) {
			attributes.add(stateof(attribute().instanceOf(INVALID).value("TRUE")));
			invalid = managed.attribute(INVALID);  //re-fetch for persistence scenario
		}
		
		stateof(invalid).description(render(errors));
	}
	
	private List<String> checkAttributes(NamedContainer<? extends Attribute> attributes) {
		
		List<String> violations = new ArrayList<>();
		
		violations.addAll(checkAttributeOccurrences(attributes));
			
		return violations;
	}
	
	private List<String> checkLinks(NamedContainer<? extends Codelink> attributes) {
		
		List<String> violations = new ArrayList<>();
		
		violations.addAll(checkLinkOccurrences(attributes));
			
		return violations;
	}
	
	private List<String> checkAttributeOccurrences(NamedContainer<? extends Attribute> attributes) {
		
		List<String> violations = new ArrayList<>();
		
		Map<String,Integer> cards = cardinalities(attributes);
		
		List<String> processed = new ArrayList<>();
		
		for (Attribute attr : attributes) {
			
			if (!attr.is(INHERITABLE))
				continue;
			
			AttributeDefinition def = attr.definition();
			
			if (processed.contains(def.id()))
				continue;
				
			Integer card = cards.get(def.id()); 
			
			if (card==null)
				card = 0;
			
			if (!def.range().inRange(card))
				violations.add(format(occurrenceViolation,attr.qname(),card,def.range())); 
				
			processed.add(def.id());
		}
			
		return violations;
	}
	
	private List<String> checkLinkOccurrences(NamedContainer<? extends Codelink> links) {
		
		List<String> violations = new ArrayList<>();
		
		Map<String,Integer> cards = cardinalities(links);
		
		List<String> processed = new ArrayList<>();
		
		for (Codelink attr : links) {
			
			LinkDefinition def = attr.definition();
			
			if (processed.contains(def.id()))
				continue;
				
			Integer card = cards.get(def.id()); 
			
			if (card==null)
				card = 0;
			
			if (!def.range().inRange(card))
				violations.add(format(occurrenceViolation,attr.qname(),card,def.range())); 
				
			processed.add(def.id());
		}
			
		return violations;
	}
	
	
	//helper 
	private Map<String,Integer> cardinalities(Container<? extends Defined<?>> defined) {
		
		Map<String,Integer> cards = new HashMap<>();
		
		for (Defined<?> entity : defined) {
			Identified def = entity.definition();
			Integer card = cards.get(def.id());	
			cards.put(def.id(), card==null? 1 : card+1);
		}
		
		return cards;
	}
	
//	private void validateLinks(Codelist codelist,Iterable<? extends Code> codes,Report report) {
//	
//		Codelist.Private list = reveal(codelist);
//
//		Map<String,LinkDefinition> links = new HashMap<>();
//		
//		for (LinkDefinition link : list.links())
//			links.put(link.id(),link);
//		
//		for (Code code : codes)
//			validateLinks(reveal(code), links, report);
//		
//	}
//	
//	private void validateLinks(Code.Private code, Map<String,LinkDefinition> linktypes, Report report) {
//		
//		//count link type occurrences
//		Map<String,Integer> counts = new HashMap<>();
//		
//		for (Codelink link : code.links()) {
//			
//			LinkDefinition type = linktypes.get(link.definition().id());
//	
//			if (type==null)
//					continue;
//			
//			Integer count = counts.get(type);
//			counts.put(type.id(), count==null? 1 : count+1);
//		
//		}
//		
//		for (LinkDefinition type : linktypes.values()) {
//			int count = counts.containsKey(type.id())? counts.get(type.id()) : 0;  
//			if (!type.range().inRange(count))
//				report.log(
//						item(code.id(),format(codeLinkError,code.qname(), code.id(),type.qname(),type.range())
//				)).as(ERROR);
//		}
//	}

	
	private String render(List<String> violations) {
		
		StringBuilder builder = new StringBuilder();
		
		for (String violation : violations) {
			
			if (builder.length()>0)
				builder.append("\n\n");
			
			builder.append(format("❌️️ %s\n   (%s on %s) ",violation,currentUser().name(), time()));
			
		}
		
		return builder.toString();
		
	}
}

package org.cotrix.application.validation;

import static java.lang.String.*;
import static org.cotrix.application.validation.Violation.*;
import static org.cotrix.common.CommonUtils.*;
import static org.cotrix.domain.attributes.CommonDefinition.*;
import static org.cotrix.domain.attributes.Facet.*;
import static org.cotrix.domain.dsl.Codes.*;
import static org.cotrix.domain.managed.ManagedCode.*;
import static org.cotrix.domain.utils.DomainUtils.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.cotrix.common.CommonUtils;
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
import org.cotrix.domain.validation.Constraint;
import org.cotrix.domain.values.ValueType;

import com.google.gson.reflect.TypeToken;


@Singleton
public class Validator {

	static String  occurrenceViolation = "%s occurs %s time(s), violating occurences constraint [%s]";
	static String  valueViolation = "%s's value '%s' violates constraint [%s]";

	public void check(Iterable<Code> codes) {
	
		notNull("codes",codes);
		
		for (Code code : codes)
			check(reveal(code));
	}
	
	private void check(Code.Private code) {
		
		NamedStateContainer<Attribute.State> attributes = code.state().attributes();
		
		List<Violation> violations = new ArrayList<>();
		
		checkAttributes(violations,code.attributes());
		checkLinks(violations,code.links());
		
		ManagedCode managed = manage(code);
		
		Attribute invalid = managed.attribute(INVALID);
		
		if (violations.isEmpty()) {
			
			if (has(invalid))
				attributes.remove(invalid.id());
			
			return;
		}
		
		Type type = new TypeToken<List<Violation>>(){}.getType();
		
		if (hasno(invalid)) {
			
			attributes.add(stateof(attribute().instanceOf(INVALID)));
			invalid = managed.attribute(INVALID);
		}
		else {
			
			List<Violation> existingViolations = jsonBinder().fromJson(invalid.value(),type) ;
			
			for (Violation violation : existingViolations) {
				violations.remove(violation);
				violations.add(violation);
			}
			
			Collections.sort(violations);
			
		}
		
		stateof(invalid).value(CommonUtils.jsonBinder().toJson(violations,type));
	}
	
	private void checkAttributes(List<Violation> violations,NamedContainer<? extends Attribute> attributes) {
		
		checkAttributeOccurrences(violations,attributes);
		checkAttributeValues(violations,attributes);
			
	}
	
	private void checkLinks(List<Violation> violations,NamedContainer<? extends Codelink> attributes) {
		
		
		checkLinkOccurrences(violations,attributes);
	}
	
	private void checkAttributeValues(List<Violation> violations, NamedContainer<? extends Attribute> attributes) {
	
		for (Attribute attr : attributes) {
			
			AttributeDefinition def = attr.definition();
			
			ValueType type = def.valueType();
			
			for (Constraint constraint : type.constraints())
				if (!constraint.isMetBy(attr.value())) {
					Violation v = violation(attr);
					v.description(format(valueViolation,def.qname(),attr.value(),constraint));
					violations.add(v);
				}
		}	
	}
	
	private void checkAttributeOccurrences(List<Violation> violations, NamedContainer<? extends Attribute> attributes) {
		
		Map<String,Integer> cards = cardinalities(attributes);
		
		List<String> processed = new ArrayList<>();
		
		for (Attribute attr : attributes) {
			
			if (!attr.is(VALIDATABLE))
				continue;
			
			AttributeDefinition def = attr.definition();
			
			if (processed.contains(def.id()))
				continue;
				
			Integer card = cards.get(def.id()); 
			
			if (card==null)
				card = 0;
			
			if (!def.range().inRange(card)) {
				Violation v = violation(def);
				v.description(format(occurrenceViolation,attr.qname(),card,def.range()));
				violations.add(v); 
			}
			processed.add(def.id());
		}
	}
	
	private void checkLinkOccurrences(List<Violation> violations,NamedContainer<? extends Codelink> links) {
		
		Map<String,Integer> cards = cardinalities(links);
		
		List<String> processed = new ArrayList<>();
		
		for (Codelink attr : links) {
			
			LinkDefinition def = attr.definition();
			
			if (processed.contains(def.id()))
				continue;
				
			Integer card = cards.get(def.id()); 
			
			if (card==null)
				card = 0;
			
			if (!def.range().inRange(card)) {
				
				Violation v = violation(def);
				v.description(format(occurrenceViolation,attr.qname(),card,def.range()));
				violations.add(v); 
			}
			processed.add(def.id());
		}
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
	
	
	//helper
	private boolean has(Object o) {
		return o!=null;
	}
	
	private boolean hasno(Object o) {
		return o==null;
	}
}

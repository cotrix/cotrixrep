package org.cotrix.application.validation;

import static java.lang.String.*;
import static org.cotrix.application.validation.Violation.*;
import static org.cotrix.domain.attributes.Facet.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.AttributeDefinition;
import org.cotrix.domain.attributes.Attributes;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Container.AttributeDefinitions;
import org.cotrix.domain.common.Container.LinkDefinitions;
import org.cotrix.domain.common.Container.Links;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;
import org.cotrix.domain.validation.Constraint;


@Singleton
public class Validator {

	static String  nameViolation = "code has no name.";
	static String  occurrenceViolation = "%s occurs %s time(s), violating occurences constraint [%s]";
	static String  valueViolation = "%s's value '%s' violates constraint [%s]";

	
	public List<Violation> check(Codelist list, Code code) {
		
		List<Violation> violations = new ArrayList<>();
		
		if (code.qname().getLocalPart().isEmpty())
			violations.add(new Violation(nameViolation));
		
		checkAttributes(violations,list.definitions(), code.attributes());
		checkLinks(violations,list.links(),code.links());
		
		return violations;
	}
	
	private void checkAttributes(List<Violation> violations,AttributeDefinitions defs, Attributes attributes) {
		
		
		//safer not to rely on definition equality 
		Map<String,AttributeDefinition> defmap = new HashMap<String,AttributeDefinition>();
		Map<String,Integer> cards = new HashMap<String, Integer>();
		
		for (AttributeDefinition def : defs) {
			defmap.put(def.id(),def);
			cards.put(def.id(), 0);
		}
		
		for (Attribute attr : attributes) {
			
			if (!attr.is(VALIDATABLE))
				continue;

			AttributeDefinition def = attr.definition();

			//track occurrence
			Integer card = cards.get(def.id());
			
			if (card!=null)
				cards.put(def.id(),card+1);
			
			//check constraints
			for (Constraint constraint : def.valueType().constraints())
				if (!constraint.isMetBy(attr.value())) {
					Violation v = violation(attr);
					v.description(format(valueViolation,def.qname(),attr.value(),constraint));
					violations.add(v);
				}
			
		}
		
		for (AttributeDefinition def : defmap.values()) {
			if (!def.range().inRange(cards.get(def.id()))) {
				Violation v = violation(def);
				v.description(format(occurrenceViolation,def.qname(),cards.get(def.id()),def.range()));
				violations.add(v); 
			}			
		}
			
	}
	
	private void checkLinks(List<Violation> violations,LinkDefinitions defs, Links links) {
	
		//safer not to rely on definition equality 
		Map<String,LinkDefinition> defmap = new HashMap<String,LinkDefinition>();
		Map<String,Integer> cards = new HashMap<String, Integer>();
		
		for (LinkDefinition def : defs) {
			defmap.put(def.id(),def);
			cards.put(def.id(), 0);
		}
		
		for (Link attr : links) {
			
			LinkDefinition def = attr.definition();
			
			//track occurrence
			Integer card = cards.get(def.id());
			
			if (card!=null)
				cards.put(def.id(),card+1);
		}

		for (LinkDefinition def : defmap.values()) {
			if (!def.range().inRange(cards.get(def.id()))) {
				Violation v = violation(def);
				v.description(format(occurrenceViolation,def.qname(),cards.get(def.id()),def.range()));
				violations.add(v); 
			}			
		}
	}
	
	
	
	
}

package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.changelog.EditorialEvent.Type.*;
import static org.cotrix.application.changelog.ChangelogEntry.*;

import java.util.ArrayList;
import java.util.List;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Facet;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.common.NamedContainer;

public class ChangelogProducer {
	
	private final Changelog log = new Changelog();
	
	public Changelog changesBetween(Code origin, Code current) {
		
		addCodeChanges(origin,current);
		addAttributesChanges(origin,current);
		addLinkChanges(origin,current);
		
		return log;
	}

	private void addCodeChanges(Code origin, Code current){
		
		add(entry(current), "name", origin.qname(), current.qname());

	}
	
	private void addAttributesChanges(Code origin, Code current){
		
		NamedContainer<? extends Attribute> attributesBefore = origin.attributes();

		List<String> matched = new ArrayList<String>();
		
		for (Attribute attr :  current.attributes()) {
		
			if (!attr.is(Facet.LOGGABLE))
				continue;
			
			String id = attr.id();
			
			if (attributesBefore.contains(id)) {
				
				addAttributeChanges(current,attributesBefore.lookup(id), attr);
				
				matched.add(id);
				
			}
			else {
				
				ChangelogEntry e = entry(ADDITION,current,attr).to(attr.qname());
				
				e.description("added attribute");
				
				log.add(e);
			}
		}
	
		for (Attribute attr :  origin.attributes())
			if (attr.is(Facet.LOGGABLE) && !matched.contains(attr.id())) {
			
				ChangelogEntry e = entry(DELETION,current,attr).from(attr.qname().getLocalPart());
				
				e.description("removed attribute");
				
				log.add(e);
			}
	}
	
	private void addLinkChanges(Code origin, Code current){
		
		
		NamedContainer<? extends Codelink> linksBefore = origin.links();

		List<String> matched = new ArrayList<String>();

		
		
		for (Codelink link :  current.links()) {
			
			String id = link.id();
			
			if (linksBefore.contains(id)) {
				
				addLinkChanges(current, linksBefore.lookup(id), link);
				
				matched.add(id);
			}
			else {
			
				ChangelogEntry e = entry(ADDITION,current,link).to(link.qname());
				e.description("added link");
				log.add(e);
			
			}
		}

		for (Codelink link :  origin.links())
			
			if (!matched.contains(link.id())) {
			 
				ChangelogEntry e = entry(DELETION,current,link).from(link.qname());
				e.description("removed link");
				log.add(e);
			}
	}
	
	private void addAttributeChanges(Code code,Attribute origin, Attribute current){
		
		add(entry(code,current), "name", origin.qname(), current.qname());
		add(entry(code,current), "value", origin.value(), current.value());
		add(entry(code,current), "type", origin.type(), current.type());
		add(entry(code,current), "language", origin.language(), current.language());
		
	}
	
	private void addLinkChanges(Code code,Codelink origin, Codelink current){
		
		LinkDefinition origindef = origin.definition();
		LinkDefinition currentdef = current.definition();
		
		add(entry(code, current), "name", origindef.qname(), origindef.qname());
		
		String tformat = "%s $s";
		String torigin = format(tformat,origindef.target().qname(),origindef.target().version());
		String tcurrent = format(tformat,currentdef.target().qname(),currentdef.target().version());
		
		add(entry(code, current), "target list",torigin, tcurrent);
		add(entry(code, current), "target code",origin.target().qname(), current.target().qname());
		add(entry(code, current), "value",origin.valueAsString(), current.valueAsString());
		
	}
	
	private void add(ChangelogEntry entry, String type, Object origin, Object current){
		
		origin = origin==null?"[none]":origin;
		current = current==null?"[none]":current;
		
		if (!origin.equals(current)) {
		
			String description = format("%s: %s → %s",type,origin,current);
			
			entry.from(origin.toString()).to(current.toString()).description(description);
			
			log.add(entry);
		}
		
	}
}

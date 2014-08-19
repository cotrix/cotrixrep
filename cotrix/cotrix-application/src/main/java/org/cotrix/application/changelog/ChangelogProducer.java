package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.changelog.ChangelogEntry.*;
import static org.cotrix.application.shared.EditorialEvent.Type.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.attributes.Facet;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.common.Containers.Attributes;
import org.cotrix.domain.common.Containers.Links;
import org.cotrix.domain.links.Link;
import org.cotrix.domain.links.LinkDefinition;

public class ChangelogProducer {
	
	private final Changelog log = new Changelog();
	
	public Changelog changesBetween(Code origin, Code current) {
		
		addCodeChanges(origin,current);
		addAttributesChanges(origin,current);
		addLinkChanges(origin,current);
		
		return log;
	}

	private void addCodeChanges(Code origin, Code current){
		
		add(entry(current), "name change", origin.qname(), current.qname());

	}
	
	private void addAttributesChanges(Code origin, Code current){
		
		Attributes attributesBefore = origin.attributes();

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
		
		
		Links linksBefore = origin.links();

		List<String> matched = new ArrayList<String>();

		
		
		for (Link link :  current.links()) {
			
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

		for (Link link :  origin.links())
			
			if (!matched.contains(link.id())) {
			 
				ChangelogEntry e = entry(DELETION,current,link).from(link.qname());
				e.description("removed link");
				log.add(e);
			}
	}
	
	private void addAttributeChanges(Code code,Attribute origin, Attribute current){
		
		add(entry(code,current), "name change", origin.qname(), current.qname());
		add(entry(code,current), "value change", origin.value(), current.value());
		add(entry(code,current), "type change", origin.type(), current.type());
		add(entry(code,current), "language change", origin.language(), current.language());
		
	}
	
	private void addLinkChanges(Code code,Link origin, Link current){
		
		LinkDefinition origindef = origin.definition();
		LinkDefinition currentdef = current.definition();
		
		add(entry(code, current), "name change", origindef.qname(), origindef.qname());
		
		String tformat = "%s $s";
		String torigin = format(tformat,local(origindef.target().qname()),origindef.target().version());
		String tcurrent = format(tformat,local(currentdef.target().qname()),currentdef.target().version());
		
		add(entry(code, current), "target list change",torigin, tcurrent);
		add(entry(code, current), "target code change", origin.target().qname(), current.target().qname());
		add(entry(code, current), "value change",origin.valueAsString(), current.valueAsString());
		
	}
	
	private void add(ChangelogEntry entry, String type, Object origin, Object current){
		
		origin = origin==null?"[none]":origin;
		current = current==null?"[none]":current;
		
		if (!origin.equals(current)) {
		
			//not pretty, but it has to be after comparison.
			if (origin instanceof QName)
				origin = QName.class.cast(origin).getLocalPart();
			
			if (current instanceof QName)
				current = QName.class.cast(current).getLocalPart();
			
			String description = format("%s: %s → %s",type,origin,current);
			
			
			entry.from(origin).to(current).description(description);
			
			log.add(entry);
		}
		
	}
	
	//helper
	
	String local(QName name) {
		return name.getLocalPart();
	}
}

package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.changelog.GroupEntry.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelink;
import org.cotrix.domain.codelist.LinkDefinition;
import org.cotrix.domain.common.NamedStateContainer;

@Singleton
public class ChangelogDetector {

	
	public List<ChangelogGroup> changesBetween(Code origin, Code current) {
		
		List<ChangelogGroup> changes = new ArrayList<>();
		
		Code.State originState = reveal(origin).state();
		Code.State currentState = reveal(current).state();
		
		detectCodeChanges(changes,originState,currentState);
		detectAttributesChanges(changes,originState,currentState);
		detectLinkChanges(changes,originState,currentState);
		
		return changes;
	}

	private void detectCodeChanges(List<ChangelogGroup> changes,Code.State origin, Code.State current){
		
		ChangelogGroup group = new ChangelogGroup("code"); 
		changes.add(group);
		
		detectChange(group, "name", origin.qname(), current.qname());

	}
	
	private void detectAttributesChanges(List<ChangelogGroup> changes,Code.State origin, Code.State current){
		
		
		NamedStateContainer<Attribute.State> attributesBefore = origin.attributes();

		
		for (Attribute.State attr :  current.attributes()) {
			
			String id = attr.id();
			
			if (attributesBefore.contains(id)) {
				
				String name= (attr.language()==null? 
											attr.qname().toString()
											: 
											format("%s (%s) ",attr.qname(),attr.language()));
				
				ChangelogGroup group = new ChangelogGroup(name);
				
				changes.add(group);
				
				detectAttributeChanges(group, attributesBefore.lookup(id), attr);
			}
		}
	
	}
	
	private void detectLinkChanges(List<ChangelogGroup> changes,Code.State origin, Code.State current){
		
		
		NamedStateContainer<Codelink.State> linksBefore = origin.links();

		
		for (Codelink.State link :  current.links()) {
			
			String id = link.id();
			
			if (linksBefore.contains(id)) {
				
				LinkDefinition.State def = link.definition();
				
				String name= format("%s (%s %s) ",def.qname(),def.target().qname(),def.target().version());
				
				ChangelogGroup group = new ChangelogGroup(name);
				
				changes.add(group);
				
				detectLinkChanges(group, linksBefore.lookup(id), link);
			}
		}
	
	}
	
	private void detectAttributeChanges(ChangelogGroup group,Attribute.State origin, Attribute.State current){
		
		detectChange(group, "name", origin.qname(), current.qname());
		detectChange(group, "value", origin.value(), current.value());
		detectChange(group, "type", origin.type(), current.type());
		detectChange(group, "language", origin.language(), current.language());
		
	}
	
	private void detectLinkChanges(ChangelogGroup group,Codelink.State origin, Codelink.State current){
		
		LinkDefinition.State origindef = origin.definition();
		LinkDefinition.State currentdef = current.definition();
		
		detectChange(group, "name", origindef.qname(), origindef.qname());
		
		String tformat = "%s $s";
		String torigin = format(tformat,origindef.target().qname(),origindef.target().version());
		String tcurrent = format(tformat,currentdef.target().qname(),currentdef.target().version());
		detectChange(group, "target list",torigin, tcurrent);
		
		detectChange(group, "target code",origin.target().qname(), current.target().qname());
		
		detectChange(group, "value",origin.entity().valueAsString(), current.entity().valueAsString());
		
	}
	
	private void detectChange(ChangelogGroup group, String type, Object origin, Object current){
		
		origin = origin==null?"[none]":origin;
		current = current==null?"[none]":current;
		
		if (!origin.equals(current)) {
		
			String description = format("%s: %s → %s",type,origin,current);
			
			group.entries().add(entry(origin.toString(), current.toString(),description));
		}
		
	}
}

package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.changelog.GroupEntry.*;
import static org.cotrix.domain.dsl.Codes.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.common.NamedStateContainer;
import org.cotrix.domain.trait.Named;

@Singleton
public class ChangelogDetector {

	
	public List<ChangelogGroup> changesBetween(Code origin, Code current) {
		
		List<ChangelogGroup> changes = new ArrayList<>();
		
		Code.State originState = reveal(origin).state();
		Code.State currentState = reveal(current).state();
		
		detectCodeChanges(changes,originState,currentState);
		detectAttributesChanges(changes,originState,currentState);
		
		return changes;
	}

	private void detectCodeChanges(List<ChangelogGroup> changes,Code.State origin, Code.State current){
		
		ChangelogGroup group = new ChangelogGroup("code"); 
		changes.add(group);
		
		detectNameChange(group,origin,current);

	}
	
	private void detectAttributesChanges(List<ChangelogGroup> changes,Code.State origin, Code.State current){
		
		
		NamedStateContainer<Attribute.State> attributesBefore = origin.attributes();

		
		for (Attribute.State attr :  current.attributes()) {
			
			String id = attr.id();
			
			if (attributesBefore.contains(id)) {
				
				String name= (attr.language()==null? 
											attr.name().toString()
											: 
											format("%s (%s) ",attr.name(),attr.language()));
				
				ChangelogGroup group = new ChangelogGroup(name);
				
				changes.add(group);
				
				detectAttributeChanges(group, attributesBefore.lookup(id), attr);
			}
		}
	
	}
	
	private void detectAttributeChanges(ChangelogGroup group,Attribute.State origin, Attribute.State current){
		
		detectNameChange(group, origin, current);
		
	}
	
	
	private void detectNameChange(ChangelogGroup group,Named.State origin, Named.State current){
		
		if (!origin.name().equals(current.name())) {
		
			String description = format("name: %s → %s",origin.name(),current.name());
			
			group.entries().add(entry(origin.name().toString(), current.name().toString(),description));
		}
		
	} 
}

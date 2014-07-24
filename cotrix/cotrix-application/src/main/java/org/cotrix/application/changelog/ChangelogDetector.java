package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.changelog.GroupEntry.*;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;

@Singleton
public class ChangelogDetector {

	
	public Map<String,ChangelogGroup> changesBetween(Code code, Code changeset) {
		
		Map<String,ChangelogGroup> changes = new HashMap<>();
		
		
		detectCodeChanges(changes,code,changeset);
		detectAttributeChanges(changes,code,changeset);
		
		return changes;
	}

	private void detectCodeChanges(Map<String,ChangelogGroup> changes,Code code, Code changeset){
		
		ChangelogGroup group = new ChangelogGroup("code"); 
		changes.put(code.id(),group);
		
		detectNameChange(group,code,changeset);

	}

	private void detectNameChange(ChangelogGroup group,Code code, Code changeset){
		
		if (!code.qname().equals(changeset.qname())) {
		
			String description = format("new name: %s → %s",code.qname(),changeset.qname());
			group.entries().add(entry(code.qname().toString(), changeset.qname().toString(),description));
		}
	}
	
	private void detectAttributeChanges(Map<String,ChangelogGroup> changes,Code code, Code changeset){
		
		//TODO
	}
	
	
}

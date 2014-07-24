package org.cotrix.application.changelog;

import static java.lang.String.*;
import static org.cotrix.application.changelog.CodeChange.*;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.cotrix.domain.codelist.Code;

@Singleton
public class ChangeDetector {

	
	public Map<String,CodeChange> changesBetween(Code code, Code changeset) {
		
		Map<String,CodeChange> changes = new HashMap<>();
		
		detectNameChange(changes,code,changeset);
		
		return changes;
	}

	private void detectNameChange(Map<String,CodeChange> changes,Code code, Code changeset){
		if (!code.qname().equals(changeset.qname())) {
			String description = format("<name>: %s => %s",code.qname(),changeset.qname());
			changes.put(code.id(),change(code.qname().toString(), changeset.qname().toString(),description));
		}
	}
	
	
}

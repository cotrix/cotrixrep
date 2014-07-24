package org.cotrix.application.changelog;

import java.util.ArrayList;
import java.util.List;


public class ChangelogGroup {
	
	private final String name;
	private final List<GroupEntry> entries;
	
	
	public ChangelogGroup(String name) {
		
		this.name=name;
		this.entries=new ArrayList<GroupEntry>();
	}
	
	
	public String name() {
		return name;
	}
	
	public List<GroupEntry> entries() {
		return entries;
	}
}

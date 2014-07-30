package org.cotrix.application.changelog;

import java.util.ArrayList;
import java.util.List;

public class Changelog {

	List<ChangelogEntry> entries = new ArrayList<ChangelogEntry>();
	
	
	public List<ChangelogEntry> entries() {
		return entries;
	}
	
	
	public boolean isEmpty() {
		return entries.isEmpty();
	}
	
	public void add(ChangelogEntry entry) {
	
		entries.remove(entry);
		entries.add(entry);
			
	}
	
	public void addAll(List<ChangelogEntry> entries) {
		for (ChangelogEntry entry: entries)
			add(entry);
	}
}

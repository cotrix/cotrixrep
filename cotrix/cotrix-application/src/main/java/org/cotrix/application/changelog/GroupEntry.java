package org.cotrix.application.changelog;


public class GroupEntry {
	
	static GroupEntry entry(String from,String to, String description) {
		return new GroupEntry(from, to, description);
	}

	private final String from;
	private final String to;
	private final String description;
	
	public GroupEntry(String from, String to, String description) {
		this.from = from;
		this.to = to;
		this.description = description;
	}
	
	public String from() {
		return from;
	}
	
	public String to() {
		return to;
	}
	
	public String description() {
		return description;
	}
}

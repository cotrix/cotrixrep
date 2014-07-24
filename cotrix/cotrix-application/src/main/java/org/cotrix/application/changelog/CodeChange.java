package org.cotrix.application.changelog;


public class CodeChange {
	
	static CodeChange change(String from,String to, String description) {
		return new CodeChange(from, to, description);
	}

	private final String from;
	private final String to;
	private final String description;
	
	public CodeChange(String from, String to, String description) {
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

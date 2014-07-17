package org.cotrix.application.changelog;

public abstract class CodeChange {

	
	private final String id;
	
	public CodeChange(String id) {
		this.id=id;
	}
	
	
	
	public static class New extends CodeChange {
	
		New(String id) {
			super(id);
		}
	}
	
	public static class Removed extends CodeChange {
		
		Removed(String id) {
			super(id);
		}
	}
	
	public static class Modified extends CodeChange {
		Modified(String id) {
			super(id);
		}
	}
	
	
	
}

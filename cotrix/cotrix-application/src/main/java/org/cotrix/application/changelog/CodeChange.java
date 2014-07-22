package org.cotrix.application.changelog;

public abstract class CodeChange {

	
	private final String id;
	private final String date;
	
	CodeChange(String id,String date) {
		this.id=id;
		this.date=date;
	}
	
	public String id() {
		return id;
	}
	
	public String date() {
		return date;
	}
	
	
	public static class New extends CodeChange {
	
		New(String id, String date) {
			super(id,date);
		}
		
	}
	
	public static class Deleted extends CodeChange {
		
		Deleted(String id, String date) {
			super(id,date);
		}
	}
	
	public static class Modified extends CodeChange {
		Modified(String id,String date) {
			super(id,date);
		}
	}
	
	
	
}

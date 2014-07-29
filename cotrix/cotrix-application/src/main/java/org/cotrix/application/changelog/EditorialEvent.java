package org.cotrix.application.changelog;

public interface EditorialEvent {

	public static enum Type {ERROR,ADDITION,DELETION,CHANGE}
	
	Type type();
	
	String title();

	String subtitle();

	String description();

	String timestamp();

	String user();

}
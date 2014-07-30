package org.cotrix.application.shared;

public interface EditorialEvent {

	public static enum Type {ERROR,ADDITION,DELETION,CHANGE}
	
	Type type();
	
	String title();

	String subtitle();

	String description();

	String timestamp();

	String user();

}
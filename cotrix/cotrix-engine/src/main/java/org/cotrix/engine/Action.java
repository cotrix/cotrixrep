package org.cotrix.engine;

import java.util.List;

public interface Action {

	public static final String action_part_separator =":";
	public static final String action_instance_separator ="@";
	
	public static final String any ="*";
	
	List<String> parts();
	
	String instance();
}

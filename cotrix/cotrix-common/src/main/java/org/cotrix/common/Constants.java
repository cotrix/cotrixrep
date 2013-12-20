package org.cotrix.common;

public class Constants {

	public static final String PRODUCTION="production";
	
	
	public static final String DEFAULT_PROFILE=PRODUCTION;
	
	public static final String CONFIGURATION_FILE_PROPERTY_NAME = "cotrix.config";
	
	
	//priorities for alternative 
	public static final int DEFAULT=1;
	public static final int RUNTIME=1000;
	public static final int TEST=Integer.MAX_VALUE;
}

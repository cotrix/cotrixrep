package org.cotrix.stage.data;

import static java.util.Arrays.*;

import java.util.Collection;


public class SomeCodelists {
	
	public static final Collection<Info> CSV_CODELISTS = asList(
			
			info("/ASFIS_MINI.csv", "ASFIS", "3A_CODE", "2011"),
			info("/ASFIS_MINI.csv", "ASFIS", "3A_CODE", "2012"), 
			info("/countries.csv", "COUNTRIES", "ISO 3166-1-alpha-2 code", "1.0"));

	public static final Collection<Info> SDMX_CODELISTS = asList( 
			
			info("/CL_SPECIES.xml", "SPECIES", "", ""),
			info("/FAO_AREA.xml", "FAO AREA", "", ""));

	
	
	static Info info(String n, String cn, String ccn, String v) {
		
		Info info = new Info();
		
		info.resource = n;
		info.name = cn;
		info.code = ccn;
		info.version = v;
		
		return info;
	}

	public static class Info {
		public String resource;
		public String name;
		public String code;
		public String version;
	}

	

	
}

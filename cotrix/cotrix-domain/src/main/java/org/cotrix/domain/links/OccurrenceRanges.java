package org.cotrix.domain.links;

import org.cotrix.domain.links.OccurrenceRange.Default;

public class OccurrenceRanges {

	public static OccurrenceRange between(int min,int max) {
		return new OccurrenceRange.Default(min,max);
	}
	
	public static OccurrenceRange atleast(int min) {
		return new OccurrenceRange.Default(min);
	}
	
	public static OccurrenceRange atmost(int max) {
		return new OccurrenceRange.Default(0,max);
	}

	public static final  OccurrenceRange once = new Default(1,1);
	public static final  OccurrenceRange atmostonce = new Default(0,1);
	public static final  OccurrenceRange arbitrarily = new Default(0);
	public static final  OccurrenceRange atleastonce = new Default(1);
	
	
}

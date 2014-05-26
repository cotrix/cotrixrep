package org.cotrix.domain.common;

import org.cotrix.domain.common.Range.Default;

public class Ranges {

	public static Range between(int min,int max) {
		return new Range.Default(min,max);
	}
	
	public static Range atleast(int min) {
		return new Range.Default(min);
	}
	
	public static Range atmost(int max) {
		return new Range.Default(0,max);
	}

	public static final  Range once = new Default(1,1);
	public static final  Range atmostonce = new Default(0,1);
	public static final  Range arbitrarily = new Default(0);
	public static final  Range atleastonce = new Default(1);
	
	
}

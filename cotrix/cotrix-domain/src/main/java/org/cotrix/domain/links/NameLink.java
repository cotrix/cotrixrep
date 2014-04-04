package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Code;

public class NameLink implements ValueType {

	public static NameLink INSTANCE = new NameLink();
	
	private NameLink() {}
	
	@Override
	public Object valueIn(Code.State code) {
		
		return code.name();
	
	}
}

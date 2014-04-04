package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Code;

public class NameLink implements LinkType {

	public static NameLink INSTANCE = new NameLink();
	
	private NameLink() {}
	
	@Override
	public Object value(Code.State code) {
		return code.name();
	}
}

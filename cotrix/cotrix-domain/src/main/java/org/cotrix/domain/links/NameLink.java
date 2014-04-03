package org.cotrix.domain.links;

import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.LinkType;

public class NameLink implements LinkType {

	public static NameLink INSTANCE = new NameLink();
	
	private NameLink() {}
	
	@Override
	public Object value(Code code) {
		return code.name();
	}
}

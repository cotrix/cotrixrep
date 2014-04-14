package org.cotrix.domain.links;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cotrix.domain.codelist.Code;

public class NameLink implements ValueType {

	public static NameLink INSTANCE = new NameLink();
	
	private NameLink() {}
	
	@Override
	public Collection<Object> valueIn(String linkId, Code.State code,List<String> ids) {
		
		return Collections.<Object>singleton(code.name());
	
	}
}

package org.cotrix.domain.links;

import java.util.Collections;
import java.util.List;

import org.cotrix.domain.codelist.Code;

public class NameLink implements LinkValueType {

	public static NameLink INSTANCE = new NameLink();
	
	private NameLink() {}
	
	@Override
	public List<Object> valueIn(String linkId, Code.State code,List<String> ids) {
		
		return Collections.<Object>singletonList(code.name());
	
	}
}

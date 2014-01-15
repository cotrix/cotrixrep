package org.acme.domain;

import static org.cotrix.common.Constants.*;
import static org.cotrix.common.Utils.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.acme.SubjectProvider;
import org.cotrix.domain.codelist.Code;
import org.cotrix.domain.codelist.Codelist;
import org.cotrix.domain.common.Attribute;

@Alternative @Priority(TEST)
public class NeoSubjectProvider implements SubjectProvider {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T like(T object) {
		
		Object provided = null;
		
		if (object instanceof Codelist)
			provided = new Codelist.Private(reveal(object, Codelist.Private.class).state());
		
		else 
			if (object instanceof Code)
				provided = new Code.Private(reveal(object, Code.Private.class).state());
		
		else 
			if (object instanceof Attribute)
			provided = new Attribute.Private(reveal(object, Attribute.Private.class).state());
		
		
		else
			throw new IllegalArgumentException("cannot provide test subject for "+object);
		
		return (T) provided;
	}
	
	
}

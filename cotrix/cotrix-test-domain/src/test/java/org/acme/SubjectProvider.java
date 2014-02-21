package org.acme;

import static org.cotrix.common.Constants.*;
import static org.cotrix.domain.dsl.Codes.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.cotrix.domain.common.Attribute;
import org.cotrix.domain.common.NamedStateContainer;

//allows persistence bindings to provide equivalent of in-memory objects 
//as subjects to domain tests
public interface SubjectProvider {

	<T> T like(T object);
	
	NamedStateContainer<Attribute.State> like(Attribute.State ...attributes);
	
	@Alternative @Priority(DEFAULT)
	static class Default implements SubjectProvider {
		
		@Override
		public <T> T like(T object) {
			return object;
		}
		
		@Override
		public NamedStateContainer<Attribute.State> like(Attribute.State ... attributes) {
			return namedBeans(attributes);
		}
	}
}

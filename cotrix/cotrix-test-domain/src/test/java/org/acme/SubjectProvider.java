package org.acme;

import static java.util.Arrays.*;
import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

import org.cotrix.domain.attributes.Attribute;
import org.cotrix.domain.common.Container;
import org.cotrix.domain.memory.MContainer;

//allows persistence bindings to provide equivalent of in-memory objects 
//as subjects to domain tests
public interface SubjectProvider {

	<T> T like(T object);
	
	Container.Bean<Attribute.Bean> like(Attribute.Bean ...attributes);
	
	@Alternative @Priority(DEFAULT)
	static class Default implements SubjectProvider {
		
		@Override
		public <T> T like(T object) {
			return object;
		}
		
		@Override
		public Container.Bean<Attribute.Bean> like(Attribute.Bean ... attributes) {
			return new MContainer<>(asList(attributes));
		}
	}
}

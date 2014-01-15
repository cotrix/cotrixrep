package org.acme;

import static org.cotrix.common.Constants.*;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;

//allows persistence bindings to provide equivalent of in-memory objects 
//as subjects to domain tests
public interface SubjectProvider {

	<T> T like(T object);
	
	@Alternative @Priority(DEFAULT)
	static class Default implements SubjectProvider {
		
		@Override
		public <T> T like(T object) {
			return object;
		}
	}
}

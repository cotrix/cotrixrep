package org.cotrix.action;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public enum ResourceType implements Iterable<Action>{

	//note: cannot factor out implementation of iterator() by passing action enum values in constructor..
	// e.g. main(MainAction.values())....
	//as the values depend on the type and will not be defined yet
	
	none() {
		@Override
		public Iterator<Action> iterator() {
			return Collections.<Action>emptyList().iterator();
		}
	},
	
	application() {
		
		@Override
		public Iterator<Action> iterator() {
			return Arrays.<Action>asList(MainAction.values()).iterator();
		}
	},
	
	codelists() {
		
		@Override
		public Iterator<Action> iterator() {
			return Arrays.<Action>asList(CodelistAction.values()).iterator();
		}
	},
	
	users() {
		
		@Override
		public Iterator<Action> iterator() {
			return Arrays.<Action>asList(UserAction.values()).iterator();
		}
	};
}

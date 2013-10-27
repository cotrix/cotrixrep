package org.cotrix.action;

import java.util.Arrays;
import java.util.Iterator;

public interface ActionType extends Iterable<Action>{

	final ActionType main = new ActionType() {
		
		@Override
		public Iterator<Action> iterator() {
			return Arrays.<Action>asList(MainAction.values()).iterator();
		}
	};
	
	final ActionType codelist = new ActionType() {
		
		@Override
		public Iterator<Action> iterator() {
			return Arrays.<Action>asList(CodelistAction.values()).iterator();
		}
	};
	
	
}

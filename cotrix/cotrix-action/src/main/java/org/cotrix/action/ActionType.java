package org.cotrix.action;

import java.util.Arrays;
import java.util.Collection;

public interface ActionType {

	Collection<? extends Action> values();

	final ActionType main = new ActionType() {
		
		@Override
		public Collection<MainAction> values() {
			return Arrays.asList(MainAction.values());
		}
	};
	
	final ActionType codelist = new ActionType() {
		
		@Override
		public Collection<CodelistAction> values() {
			return Arrays.asList(CodelistAction.values());
		}
	};
	
	
}

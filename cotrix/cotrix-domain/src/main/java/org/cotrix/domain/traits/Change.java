package org.cotrix.domain.traits;

import java.util.Arrays;
import java.util.List;

/**
 * The type of incremental changes that can be applied to objects.
 * 
 * @author Fabio Simeoni
 *
 */
public enum Change {

	NEW() {
		@Override
		List<Change> compatibleWith() {
			return Arrays.asList(NEW,MODIFIED);
		}
	},
	MODIFIED() {
		@Override
		List<Change> compatibleWith() {
			return Arrays.asList(MODIFIED);
		}
	},
	DELETED(){
		@Override
		List<Change> compatibleWith() {
			return Arrays.asList(DELETED,MODIFIED);
		}
	};
	abstract List<Change> compatibleWith();
	
	public boolean canTransitionTo(Change change) {
		return compatibleWith().contains(change);
	}
	
}

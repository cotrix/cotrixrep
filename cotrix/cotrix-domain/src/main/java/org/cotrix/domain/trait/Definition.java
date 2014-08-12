package org.cotrix.domain.trait;

import org.cotrix.domain.common.Range;

public interface Definition extends Identified, Named {

	
	Range range();
	
	//----------------------------------------
	
	static interface Bean extends Described.Bean, Definition {
		
		void range(Range type);
	}
	
	//----------------------------------------
	
}

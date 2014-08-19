package org.cotrix.domain.trait;

import org.cotrix.domain.common.Range;

public interface Definition extends Identified, Named {

	
	Range range();
	
	//----------------------------------------
	
	interface Bean extends Described.Bean, Definition {
		
		void range(Range type);
	}
	
	//----------------------------------------
	
	class Private<SELF extends Private<SELF,B>, B extends Bean> extends Described.Private<SELF,B> implements Definition {

		
		public Private(B bean) {
			super(bean);
		}
		
		@Override
		public Range range() {
			return bean().range();
		}
		
		@Override
		public void update(SELF changeset) throws IllegalArgumentException, IllegalStateException {
			
			super.update(changeset);
		
			//range
			Range newrange = changeset.range();
			
			if (newrange!=null)
				bean().range(newrange);
		}
	}
	
}

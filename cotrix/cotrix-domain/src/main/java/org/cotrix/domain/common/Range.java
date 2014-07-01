package org.cotrix.domain.common;

import static org.cotrix.common.CommonUtils.*;

public interface Range {

	
	int min();
	int max();
	
	boolean inRange(int occurrence);
	
	public class Default implements Range {
		
		private final int min;
		private final int max;
		
		public Default(int min) {
			this(min, Integer.MAX_VALUE);
		}
		
		public Default(int min, int max) {
			
			verify("range bounds", min>=0 && max >0 && min<=max);
			
			this.min=min;
			this.max=max;
		}
		
		@Override
		public int min() {
			return min;
		}
		
		public int max() {
			return max;
		}
		
		public boolean inRange(int occurrence) {
			return occurrence >= min && occurrence <= max;
		}
		
		

		@Override
		public String toString() {
			return "[min=" + min + ", max=" + max + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + max;
			result = prime * result + min;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Default other = (Default) obj;
			if (max != other.max)
				return false;
			if (min != other.min)
				return false;
			return true;
		}
		
		
	}
}

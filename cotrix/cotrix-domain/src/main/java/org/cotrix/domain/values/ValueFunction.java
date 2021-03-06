package org.cotrix.domain.values;

public interface ValueFunction {

	String name();
	
	String apply(String value);
	
	
	public static abstract class Abstract implements ValueFunction {
		
		private final String name;
		
		public Abstract(String name) {
			this.name=name;
		}
		
		@Override
		public final String name() {
			return name;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			Abstract other = (Abstract) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		
	}
}

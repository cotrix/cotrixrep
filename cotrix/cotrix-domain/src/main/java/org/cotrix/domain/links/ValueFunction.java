package org.cotrix.domain.links;

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
	}
}

package org.cotrix.domain.trait;





public interface Defined<T extends Definition> {

	T definition();
	
	//----------------------------------------
	
	interface Bean<D extends BeanOf<? extends Definition>> {
		
		D definition();
		
		void definition(D def);

	}
	
	//----------------------------------------
	
	
}

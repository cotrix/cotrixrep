package org.cotrix.repository;

public interface Query<D,R> {

	
	interface Private<D,R> extends Query<D,R> {

		R execute();
		
	}
}
